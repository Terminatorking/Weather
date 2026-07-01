package ghazimoradi.soheil.weather.ui.fragments.addcity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.R.string.searchCanNotBeEmpty
import ghazimoradi.soheil.weather.data.models.addcity.ResponseCitiesList.ResponseCitiesListItem
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import ghazimoradi.soheil.weather.databinding.FragmentAddCityBinding
import ghazimoradi.soheil.weather.ui.adapters.AddCitiesAdapter
import ghazimoradi.soheil.weather.utils.base.BaseBottomSheetFragment
import ghazimoradi.soheil.weather.utils.events.EventBus
import ghazimoradi.soheil.weather.utils.events.Events
import ghazimoradi.soheil.weather.utils.network.NetworkRequest
import ghazimoradi.soheil.weather.utils.other.doWorkOnLifecycle
import ghazimoradi.soheil.weather.utils.other.setupRecyclerview
import ghazimoradi.soheil.weather.utils.other.showSnackBar
import ghazimoradi.soheil.weather.viewmodels.AddCityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AddCityFragment : BaseBottomSheetFragment<FragmentAddCityBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentAddCityBinding
        get() = FragmentAddCityBinding::inflate

    private val viewModel by viewModels<AddCityViewModel>()

    @Inject
    lateinit var cityEntity: CitiesEntity

    @Inject
    lateinit var citiesAdapter: AddCitiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            searchInpLay.setEndIconOnClickListener {
                val search = searchEdt.text.toString()
                if (isNetworkAvailable) {
                    if (search.isNotEmpty())
                        viewModel.callCitiesApi(search)
                    else
                        root.showSnackBar(getString(searchCanNotBeEmpty))
                }
            }
        }
        //Load data
        loadSearchCityData()
    }

    private fun loadSearchCityData() {
        binding.apply {
            viewModel.citiesData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible = true
                    }

                    is NetworkRequest.Success -> {
                        loading.isVisible = false
                        response.data?.let { data ->
                            if (data.isNotEmpty()) {
                                initRecyclerView(data)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.isVisible = false
                        root.showSnackBar(response.error!!)
                    }
                }
            }
        }
    }

    private fun initRecyclerView(list: List<ResponseCitiesListItem>) {
        citiesAdapter.setData(list)
        binding.citiesList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            citiesAdapter
        )

        citiesAdapter.setOnItemClickListener {

            cityEntity.lat = it.lat
            cityEntity.lon = it.lon

            val fa = it.localNames?.fa

            if (fa != null)
                cityEntity.name = fa
            else
                cityEntity.name = it.name

            viewModel.saveCity(cityEntity)

            doWorkOnLifecycle {
                EventBus.publish(Events.OnUpdateWeather(it.lat, it.lon))
            }

            this@AddCityFragment.dismiss()
        }
    }

}