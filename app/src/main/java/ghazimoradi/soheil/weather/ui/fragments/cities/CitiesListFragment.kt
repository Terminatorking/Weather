package ghazimoradi.soheil.weather.ui.fragments.cities

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.databinding.FragmentCitiesListBinding
import ghazimoradi.soheil.weather.utils.base.BaseBottomSheetFragment

@AndroidEntryPoint
class CitiesListFragment : BaseBottomSheetFragment<FragmentCitiesListBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentCitiesListBinding
        get() = FragmentCitiesListBinding::inflate

}