package ghazimoradi.soheil.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.weather.data.database.CitiesDao
import ghazimoradi.soheil.weather.data.models.addcity.ResponseCitiesList
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import ghazimoradi.soheil.weather.data.repositories.AddCityRepository
import ghazimoradi.soheil.weather.utils.network.NetworkRequest
import ghazimoradi.soheil.weather.utils.network.NetworkResponse
import ghazimoradi.soheil.weather.utils.other.SOMETHING_WENT_WRONG
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCityViewModel @Inject constructor(
    private val repository: AddCityRepository,
    private val dao: CitiesDao
) : ViewModel() {
    private val _citiesData = MutableLiveData<NetworkRequest<ResponseCitiesList>>()
    val citiesData: LiveData<NetworkRequest<ResponseCitiesList>> = _citiesData

    fun callCitiesApi(q: String) {
        viewModelScope.launch {
            try {
                _citiesData.value = NetworkRequest.Loading()
                val response = repository.getCitiesList(q)
                _citiesData.value = NetworkResponse(response).generateResponse()
            } catch (e: Exception) {
                Log.e("callCitiesApi", e.message, e)
                _citiesData.value = NetworkRequest.Error(SOMETHING_WENT_WRONG)
            }
        }
    }

    fun saveCity(entity: CitiesEntity) {
        viewModelScope.launch {
            dao.saveCity(entity)
        }
    }
}