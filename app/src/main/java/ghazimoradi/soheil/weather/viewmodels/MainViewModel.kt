package ghazimoradi.soheil.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.weather.data.models.main.ResponseCurrentWeather
import ghazimoradi.soheil.weather.data.models.main.ResponseForecast
import ghazimoradi.soheil.weather.data.repositories.MainRepository
import ghazimoradi.soheil.weather.utils.network.NetworkRequest
import ghazimoradi.soheil.weather.utils.network.NetworkResponse
import ghazimoradi.soheil.weather.utils.other.SOMETHING_WENT_WRONG
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _currentWeatherData = MutableLiveData<NetworkRequest<ResponseCurrentWeather>>()
    val currentWeatherData: LiveData<NetworkRequest<ResponseCurrentWeather>> = _currentWeatherData

    private val _forecastData = MutableLiveData<NetworkRequest<ResponseForecast>>()
    val forecastData: LiveData<NetworkRequest<ResponseForecast>> = _forecastData

    fun callCurrentWeatherApi(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _currentWeatherData.value = NetworkRequest.Loading()
                val response = repository.getCurrentWeather(lat, lon)
                _currentWeatherData.value = NetworkResponse(response).generateResponse()
            } catch (e: Exception) {
                Log.e("callCurrentWeatherApi", e.message, e)
                _currentWeatherData.value = NetworkRequest.Error(SOMETHING_WENT_WRONG)
            }
        }
    }

    fun callForecastApi(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _forecastData.value = NetworkRequest.Loading()
                val response = repository.getForecast(lat, lon)
                _forecastData.value = NetworkResponse(response).generateResponse()
            } catch (e: Exception) {
                Log.e("callCurrentWeatherApi", e.message, e)
                _forecastData.value = NetworkRequest.Error(SOMETHING_WENT_WRONG)
            }
        }
    }
}