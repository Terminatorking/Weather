package ghazimoradi.soheil.weather.ui.fragments.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import ghazimoradi.soheil.weather.databinding.FragmentCitiesListBinding
import ghazimoradi.soheil.weather.ui.adapters.CitiesAdapter
import ghazimoradi.soheil.weather.utils.base.BaseBottomSheetFragment
import ghazimoradi.soheil.weather.utils.events.EventBus
import ghazimoradi.soheil.weather.utils.events.Events
import ghazimoradi.soheil.weather.utils.other.CityClickTypes
import ghazimoradi.soheil.weather.utils.other.doWorkOnLifecycle
import ghazimoradi.soheil.weather.utils.other.setupRecyclerview
import ghazimoradi.soheil.weather.viewmodels.CitiesViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CitiesListFragment : BaseBottomSheetFragment<FragmentCitiesListBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentCitiesListBinding
        get() = FragmentCitiesListBinding::inflate

    private val viewModel by viewModels<CitiesViewModel>()

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Cities list
        viewModel.callCitiesData()
        //Load data
        loadCitiesData()
    }

    private fun loadCitiesData() {
        binding.apply {
            viewModel.citiesData.observe(viewLifecycleOwner) { cities ->
                //Visibility
                visibilityView(cities.isEmpty())
                //Fill recyclerview
                if (cities.isNotEmpty()) {
                    initRecyclerView(cities)
                }
            }
        }
    }

    private fun initRecyclerView(cities: List<CitiesEntity>) {
        citiesAdapter.setData(cities)

        binding.citiesList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            citiesAdapter
        )

        //Click
        citiesAdapter.setOnItemClickListener { data, type ->
            if (type == CityClickTypes.SELECT) {
                //Update event
                doWorkOnLifecycle {
                    EventBus.publish(Events.OnUpdateWeather(data.name, data.lat, data.lon))
                }

                //Close dialog
                this@CitiesListFragment.dismiss()
            } else {
                viewModel.deleteCity(data)
            }
        }
    }

    private fun visibilityView(isEmpty: Boolean) {
        binding.apply {
            emptyLay.isVisible = isEmpty
            containerGroup.isVisible = isEmpty.not()
        }
    }

}