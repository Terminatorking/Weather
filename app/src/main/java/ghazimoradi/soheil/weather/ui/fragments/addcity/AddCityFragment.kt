package ghazimoradi.soheil.weather.ui.fragments.addcity

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.databinding.FragmentAddCityBinding
import ghazimoradi.soheil.weather.utils.base.BaseBottomSheetFragment

@AndroidEntryPoint
class AddCityFragment : BaseBottomSheetFragment<FragmentAddCityBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentAddCityBinding
        get() = FragmentAddCityBinding::inflate

}