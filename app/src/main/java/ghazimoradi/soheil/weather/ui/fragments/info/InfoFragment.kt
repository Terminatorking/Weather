package ghazimoradi.soheil.weather.ui.fragments.info

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.R.color.*
import ghazimoradi.soheil.weather.R.drawable.*
import ghazimoradi.soheil.weather.R.string.*
import ghazimoradi.soheil.weather.data.models.info.PollutionModel
import ghazimoradi.soheil.weather.data.models.info.ResponsePollution.Data.Components
import ghazimoradi.soheil.weather.data.models.info.ResponsePollution.Data.Main
import ghazimoradi.soheil.weather.databinding.FragmentInfoBinding
import ghazimoradi.soheil.weather.ui.adapters.PollutionAdapter
import ghazimoradi.soheil.weather.utils.base.BaseBottomSheetFragment
import ghazimoradi.soheil.weather.utils.network.NetworkRequest
import ghazimoradi.soheil.weather.utils.other.*
import ghazimoradi.soheil.weather.viewmodels.InfoViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class InfoFragment : BaseBottomSheetFragment<FragmentInfoBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate

    private val viewModel by viewModels<InfoViewModel>()
    private val args by navArgs<InfoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Args
            args.data.let { data ->
                //Call api
                doWorkOnLifecycle {
                    delay(500.milliseconds)
                    data.coord?.let { coord ->
                        viewModel.callPollutionApi(coord.lat!!, coord.lon!!)
                    }
                }

                //Weather
                data.weather?.let { weather ->
                    if (weather.isNotEmpty()) {
                        weather[0]?.let {
                            //Info
                            infoTxt.text = it.description
                            //Image
                            val image = "$BASE_URL_IMAGE${it.icon}$PNG_IMAGE"
                            iconImg.loadImage(image)
                        }
                    }
                }
                //Main
                data.main?.let { main ->
                    tempTxt.text = "${main.temp?.roundToInt()}${getString(degreeCelsius)}"
                    TempInfoTxt.text =
                        "${main.tempMin?.roundToInt()}${getString(degree)}    " +
                                "${main.tempMax?.roundToInt()}${getString(degree)}"
                    //Include
                    weatherLay.apply {
                        feelCountTxt.text = "${main.feelsLike?.roundToInt()}${getString(degreeCelsius)}"
                        pressureCountTxt.text = "${main.humidity}%"
                    }
                }
                //Wind
                data.wind?.let { wind ->
                    weatherLay.windCountTxt.text = "${wind.speed?.roundToInt()} ${getString(km_s)}"
                }
            }
        }
        //Load data
        loadPollutionData()
    }

    @Inject
    lateinit var pollutionAdapter: PollutionAdapter
    private fun loadPollutionData() {
        binding.apply {
            viewModel.pollutionData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, pollutionCard)
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible(false, pollutionCard)
                        response.data?.list?.let { list ->
                            if (list.isNotEmpty()) {
                                list[0].let { myData ->
                                    pollutionLay.apply {
                                        //Image
                                        pollutionIconImg.apply {
                                            setImageResource(pollutionIcon(myData.main))
                                            //Tint
                                            imageTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    pollutionColors(myData.main)
                                                )
                                            )
                                        }
                                        //Info
                                        pollutionInfoTxt.apply {
                                            text = pollutionMessage(myData.main)
                                            setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    pollutionColors(myData.main)
                                                )
                                            )
                                        }
                                        //Shadow
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            pollutionCard.apply {
                                                outlineAmbientShadowColor =
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        pollutionColors(myData.main)
                                                    )
                                                outlineSpotShadowColor =
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        pollutionColors(myData.main)
                                                    )
                                            }
                                        }
                                        //Pollution
                                        initRecyclerView(fillPollutionData(myData.components))
                                    }
                                }
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible(false, pollutionCard)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun pollutionColors(data: Main): Int {
        return when (data.aqi) {
            1 -> green
            2 -> yellow
            3 -> orange
            4 -> red
            5 -> purple
            else -> 0
        }
    }

    private fun pollutionIcon(data: Main): Int {
        return when (data.aqi) {
            1 -> face_smile_hearts
            2, 3 -> face_clouds
            4, 5 -> face_mask
            else -> 0
        }
    }

    private fun pollutionMessage(data: Main): String {
        return when (data.aqi) {
            1 -> getString(messageAQI1)
            2 -> getString(messageAQI2)
            3, 4 -> getString(messageAQI3_4)
            5 -> getString(messageAQI5)
            else -> ""
        }
    }

    private fun fillPollutionData(data: Components): MutableList<PollutionModel> {
        val list = mutableListOf<PollutionModel>()
        list.add(PollutionModel(getString(co), data.co))
        list.add(PollutionModel(getString(no2), data.no2))
        list.add(PollutionModel(getString(o3), data.o3))
        list.add(PollutionModel(getString(so2), data.so2))
        list.add(PollutionModel(getString(pm2_5), data.pm25))
        list.add(PollutionModel(getString(pm10), data.pm10))
        return list
    }

    private fun initRecyclerView(list: List<PollutionModel>) {
        pollutionAdapter.setData(list)
        binding.pollutionLay.pollutionList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            pollutionAdapter
        )
    }
}