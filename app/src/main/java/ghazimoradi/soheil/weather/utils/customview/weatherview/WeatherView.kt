package ghazimoradi.soheil.weather.utils.customview.weatherview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.jinatonic.confetti.ConfettiManager
import ghazimoradi.soheil.weather.utils.customview.weatherview.confetti.ConfettoInfo
import ghazimoradi.soheil.weather.utils.customview.weatherview.confetti.MutableRectSource
import ghazimoradi.soheil.weather.utils.customview.weatherview.confetti.WeatherConfettoGenerator
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

@Suppress("unused")
class WeatherView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    val confettiSource: MutableRectSource = MutableRectSource(0, 0)
    val confettiManager: ConfettiManager

    var angle: Int = 0
        set(value) {
            field = value
            angleRadians = Math.toRadians(value.toDouble())
            updateVelocities()
        }

    var scaleFactor: Float = 1.0f
        set(value)
        {
            field = value
            confettoInfo.scaleFactor = value
        }

    var angleRadians: Double = 0.0
        private set

    var speed: Int = 0
        set(value) {
            field = value
            updateVelocities()
        }

    var fadeOutPercent: Float = 1f

    var emissionRate: Float = 0f
        set(value) {
            field = value
            updateEmissionRate()
        }

    var precipType: PrecipType = PrecipType.CLEAR
        set(value) {
            field = value
            confettoInfo.precipType = value
        }

    fun setCustomBitmap(bitmap: Bitmap){
        confettoInfo.precipType = PrecipType.CUSTOM
        confettoInfo.customBitmap = bitmap
    }

    private val confettoInfo = ConfettoInfo(PrecipType.CLEAR, 1.0f)

    init {
        confettiManager = ConfettiManager(context, WeatherConfettoGenerator(confettoInfo), confettiSource, this)
            .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
            .enableFadeOut { input -> (fadeOutPercent - input).coerceIn(0f, 1f) }
            .animate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        setConfettiBoundsToSelf()
    }

    fun setWeatherData(weatherData: WeatherData) {
        precipType = weatherData.precipType
        emissionRate = weatherData.emissionRate
        speed = weatherData.speed
        resetWeather()
    }

    fun resetWeather() {
        confettiManager.animate()
    }

    private fun setConfettiBoundsToSelf() {
        val offscreenSpawnDistance = tan(angleRadians).coerceIn(-5.0, 5.0) * height // Coerce to prevent asymptotes of the tan() function breaking things

        confettiManager.setBound(Rect(0, 0, width, height))
        confettiSource.setBounds((-offscreenSpawnDistance).toInt().coerceAtMost(0), 0, (width - offscreenSpawnDistance).toInt().coerceAtLeast(width), 0)
    }

    private fun updateEmissionRate() {
        confettiManager.setEmissionRate(emissionRate)
    }

    private fun updateVelocities() {
        val yVelocity = cos(angleRadians).toFloat() * speed
        val xVelocity = sin(angleRadians).toFloat() * speed

        confettiManager
            .setVelocityY(yVelocity, yVelocity * .05F)
            .setVelocityX(xVelocity, xVelocity * .05F)
            .setInitialRotation(-angle)
        setConfettiBoundsToSelf()
    }
}