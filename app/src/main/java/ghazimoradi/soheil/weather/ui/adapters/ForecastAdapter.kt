package ghazimoradi.soheil.weather.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.weather.R
import ghazimoradi.soheil.weather.utils.base.BaseAdapter
import ghazimoradi.soheil.weather.data.models.main.ResponseForecast.Data
import ghazimoradi.soheil.weather.databinding.ItemForecastBinding
import ghazimoradi.soheil.weather.utils.other.BASE_URL_IMAGE
import ghazimoradi.soheil.weather.utils.other.PNG_IMAGE
import ghazimoradi.soheil.weather.utils.other.convertToDayName
import ghazimoradi.soheil.weather.utils.other.loadImage

@SuppressLint("SetTextI18n")
class ForecastAdapter : BaseAdapter<ItemForecastBinding, Data>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemForecastBinding
        get() = ItemForecastBinding::inflate

    override fun bindData(item: Data) {
        binding.apply {
            //Date
            item.dtTxt?.let { date ->
                val dayName = date.split(" ")[0].convertToDayName()
                val hour = date.split(" ")[1].dropLast(3)
                dateTxt.text = "$dayName\n$hour"
            }
            //Image
            val image = "$BASE_URL_IMAGE${item.weather?.get(0)?.icon}$PNG_IMAGE"
            iconImg.loadImage(image)
            //Temp
            item.main?.let { main ->
                tempTxt.text = "${main.temp}${context.getString(R.string.degree)}"
                humidityTxt.text = "${main.humidity}%"
            }
        }
    }

}