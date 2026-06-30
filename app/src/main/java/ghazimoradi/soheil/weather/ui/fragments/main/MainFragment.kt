package ghazimoradi.soheil.weather.ui.fragments.main

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.weather.databinding.FragmentMainBinding
import ghazimoradi.soheil.weather.utils.base.BaseFragment

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate
}