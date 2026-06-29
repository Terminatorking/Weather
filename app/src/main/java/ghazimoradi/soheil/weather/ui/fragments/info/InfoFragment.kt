package ghazimoradi.soheil.weather.ui.fragments.info

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.databinding.FragmentInfoBinding
import ghazimoradi.soheil.weather.utils.base.BaseBottomSheetFragment

@AndroidEntryPoint
class InfoFragment : BaseBottomSheetFragment<FragmentInfoBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentInfoBinding
        get() = FragmentInfoBinding::inflate

}