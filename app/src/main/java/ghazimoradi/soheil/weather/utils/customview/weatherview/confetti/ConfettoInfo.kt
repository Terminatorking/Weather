package ghazimoradi.soheil.weather.utils.customview.weatherview.confetti

import android.graphics.Bitmap
import ghazimoradi.soheil.weather.utils.customview.weatherview.PrecipType

class ConfettoInfo(
    var precipType: PrecipType,
    var scaleFactor: Float,
    var customBitmap: Bitmap? = null
)