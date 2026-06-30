package ghazimoradi.soheil.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.weather.data.models.info.PollutionModel
import ghazimoradi.soheil.weather.databinding.ItemPollutionBinding
import ghazimoradi.soheil.weather.utils.base.BaseAdapter
import javax.inject.Inject

class PollutionAdapter @Inject constructor() : BaseAdapter<ItemPollutionBinding, PollutionModel>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemPollutionBinding
        get() = ItemPollutionBinding::inflate

    override fun bindData(item: PollutionModel) {
        binding.apply {
            pollutionTitle.text = item.title
            pollutionCount.text = item.count.toString()
        }
    }

}