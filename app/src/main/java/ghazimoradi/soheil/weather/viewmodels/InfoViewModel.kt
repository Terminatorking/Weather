package ghazimoradi.soheil.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.weather.data.models.info.ResponsePollution
import ghazimoradi.soheil.weather.data.repositories.InfoRepository
import ghazimoradi.soheil.weather.utils.network.NetworkRequest
import ghazimoradi.soheil.weather.utils.network.NetworkResponse
import ghazimoradi.soheil.weather.utils.other.SOMETHING_WENT_WRONG
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val repository: InfoRepository
) : ViewModel() {
    private val _pollutionData = MutableLiveData<NetworkRequest<ResponsePollution>>()
    val pollutionData: LiveData<NetworkRequest<ResponsePollution>> = _pollutionData

    fun callPollutionApi(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _pollutionData.value = NetworkRequest.Loading()
                val response = repository.getPollution(lat, lon)
                _pollutionData.value = NetworkResponse(response).generateResponse()
            } catch (e: Exception) {
                Log.e("callPollutionApi", e.message, e)
                _pollutionData.value = NetworkRequest.Error(SOMETHING_WENT_WRONG)
            }
        }
    }
}