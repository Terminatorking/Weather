package ghazimoradi.soheil.weather.ui.fragments.main

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import coil3.request.crossfade
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.R.color.black
import ghazimoradi.soheil.weather.R.color.white
import ghazimoradi.soheil.weather.R.drawable.bg_cloud
import ghazimoradi.soheil.weather.R.drawable.bg_haze
import ghazimoradi.soheil.weather.R.drawable.bg_night
import ghazimoradi.soheil.weather.R.drawable.bg_rain
import ghazimoradi.soheil.weather.R.drawable.bg_snow
import ghazimoradi.soheil.weather.R.drawable.bg_sun
import ghazimoradi.soheil.weather.R.id.actionToAddCity
import ghazimoradi.soheil.weather.R.id.actionToCitiesList
import ghazimoradi.soheil.weather.R.string.degree
import ghazimoradi.soheil.weather.R.string.degreeCelsius
import ghazimoradi.soheil.weather.data.models.main.ResponseForecast.Data
import ghazimoradi.soheil.weather.databinding.FragmentMainBinding
import ghazimoradi.soheil.weather.ui.adapters.ForecastAdapter
import ghazimoradi.soheil.weather.utils.base.BaseFragment
import ghazimoradi.soheil.weather.utils.customview.weatherview.PrecipType
import ghazimoradi.soheil.weather.utils.events.EventBus
import ghazimoradi.soheil.weather.utils.events.Events
import ghazimoradi.soheil.weather.utils.network.NetworkRequest
import ghazimoradi.soheil.weather.utils.other.doWorkOnLifecycle
import ghazimoradi.soheil.weather.utils.other.isVisible
import ghazimoradi.soheil.weather.utils.other.setTint
import ghazimoradi.soheil.weather.utils.other.setupRecyclerview
import ghazimoradi.soheil.weather.utils.other.showSnackBar
import ghazimoradi.soheil.weather.viewmodels.MainViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToInt

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    private val viewModel by viewModels<MainViewModel>()
    private val calendar by lazy { Calendar.getInstance() }

    private var lat = 34.0204789
    private var lon = -118.4117326

    private lateinit var locationClient: FusedLocationProviderClient

    @Inject
    lateinit var forecastAdapter: ForecastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            menuImg.setOnClickListener {
                findNavController().navigate(actionToCitiesList)
            }

            addImg.setOnClickListener {
                findNavController().navigate(actionToAddCity)
            }
        }

        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        getLocation()

        loadCurrentWeatherData()
        loadForecastData()

        doWorkOnLifecycle {
            EventBus.subscribe<Events.OnUpdateWeather> {
                callApis(it.lat!!, it.lon!!)
            }
        }
    }

    private fun callApis(lat: Double, lon: Double) {
        viewModel.callCurrentWeatherApi(lat, lon)
        viewModel.callForecastApi(lat, lon)
    }

    private fun loadCurrentWeatherData() {
        binding.apply {
            viewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, container)
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible(false, container)
                        response.data?.let { data ->

                            showAllTxt.setOnClickListener {
                                val direction = MainFragmentDirections.actionToInfo(data)
                                findNavController().navigate(direction)
                            }

                            cityName.text = data.name

                            data.weather?.let { weathers ->
                                if (weathers.isNotEmpty()) {
                                    weathers[0]?.let { weather ->
                                        infoTxt.text = weather.description

                                        val image = weather.icon?.let {
                                            setDynamicallyWallpaper(it)
                                        }

                                        bgImg.load(if (isNightNow()) bg_night else image) {
                                            crossfade(true)
                                            crossfade(100)
                                        }

                                        val color = if (isNightNow() || image != bg_sun) {
                                            white
                                        } else {
                                            black
                                        }

                                        binding.apply {
                                            requireContext().apply {
                                                menuImg.setTint(color)
                                                addImg.setTint(color)

                                                cityName.setTextColor(getColor(color))
                                                infoTxt.setTextColor(getColor(color))
                                                tempTxt.setTextColor(getColor(color))

                                                TempInfoTxt.apply {
                                                    compoundDrawables.forEach {
                                                        if (it != null) {
                                                            it.setTint(getColor(color))
                                                        }
                                                    }

                                                    setTextColor(getColor(color))
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            data.main?.let { main ->
                                tempTxt.text = "${main.temp?.roundToInt()}${getString(degreeCelsius)}"
                                TempInfoTxt.text = "${main.tempMin?.roundToInt()}${getString(degree)}    " +
                                        "${main.tempMax?.roundToInt()}${getString(degree)}"
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible(false, container)
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynamicallyWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                bg_sun
            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
                bg_cloud
            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)
                bg_rain
            }

            "13" -> {
                initWeatherView(PrecipType.SNOW)
                bg_snow
            }

            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                bg_haze
            }

            else -> 0
        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle = 20
            emissionRate = 100.0f
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val snackBar =
                Snackbar.make(binding.root, "درحال دریافت موقعیت مکانی", Snackbar.LENGTH_INDEFINITE)

            snackBar.show()

            locationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    snackBar.dismiss()
                    location?.let {
                        lat = it.latitude
                        lon = it.longitude
                    }
                    callApis(lat, lon)
                    Log.i("getLocation", "lat : $lat lon: $lon")
                }.addOnFailureListener {
                    Log.e("getLocation", it.message, it)
                }
        }
    }

    private fun loadForecastData() {
        binding.apply {
            viewModel.forecastData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {}

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            if (data.list.isNotEmpty())
                                initRecyclerView(data.list)
                        }
                    }

                    is NetworkRequest.Error -> {
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecyclerView(cities: List<Data>) {
        forecastAdapter.setData(cities)

        binding.forecastList.setupRecyclerview(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                true
            ), forecastAdapter
        )
    }
}