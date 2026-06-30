package ghazimoradi.soheil.weather.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.weather.data.models.addcity.ResponseCitiesList
import ghazimoradi.soheil.weather.databinding.ItemCitiesBinding
import ghazimoradi.soheil.weather.utils.base.BaseAdapter
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class AddCitiesAdapter @Inject constructor() :
    BaseAdapter<ItemCitiesBinding, ResponseCitiesList.ResponseCitiesListItem>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemCitiesBinding
        get() = ItemCitiesBinding::inflate

    private var onItemClickListener: ((ResponseCitiesList.ResponseCitiesListItem) -> Unit)? = null

    override fun bindData(item: ResponseCitiesList.ResponseCitiesListItem) {
        binding.apply {
            val fa = item.localNames?.fa

            if (fa != null)
                citiesNameTxt.text = "$fa - ${item.country}"
            else
                citiesNameTxt.text = "${item.name} - ${item.country}"

            root.setOnClickListener {
                //Click
                onItemClickListener?.let {
                    it(item)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (ResponseCitiesList.ResponseCitiesListItem) -> Unit) {
        onItemClickListener = listener
    }

}