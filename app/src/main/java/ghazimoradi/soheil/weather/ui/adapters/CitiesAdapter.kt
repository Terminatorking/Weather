package ghazimoradi.soheil.weather.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import ghazimoradi.soheil.weather.databinding.ItemCitiesBinding
import ghazimoradi.soheil.weather.utils.base.BaseAdapter
import ghazimoradi.soheil.weather.utils.other.CityClickTypes
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class CitiesAdapter @Inject constructor() : BaseAdapter<ItemCitiesBinding, CitiesEntity>() {

    private var onItemClickListener: ((CitiesEntity, CityClickTypes) -> Unit)? = null

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemCitiesBinding
        get() = ItemCitiesBinding::inflate

    override fun bindData(item: CitiesEntity) {
        binding.apply {
            //Name
            citiesNameTxt.text = item.name
            //Delete
            trashImg.apply {
                isVisible = true
                setOnClickListener {
                    onItemClickListener?.let {
                        it(item, CityClickTypes.DELETE)
                    }
                }
            }
            //Click
            root.setOnClickListener {
                //Click
                onItemClickListener?.let {
                    it(item, CityClickTypes.SELECT)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (CitiesEntity, CityClickTypes) -> Unit) {
        onItemClickListener = listener
    }
}
