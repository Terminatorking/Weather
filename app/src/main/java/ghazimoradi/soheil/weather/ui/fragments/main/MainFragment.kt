package ghazimoradi.soheil.weather.ui.fragments.main

import android.view.LayoutInflater
import ghazimoradi.soheil.weather.databinding.FragmentMainBinding
import ghazimoradi.soheil.weather.utils.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

}