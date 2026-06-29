package ghazimoradi.soheil.weather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import ghazimoradi.soheil.weather.data.repositories.CitiesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val repository: CitiesRepository
) : ViewModel() {
    private val _citiesData = MutableLiveData<List<CitiesEntity>>()
    val citiesData: LiveData<List<CitiesEntity>> = _citiesData

    fun callCitiesData() {
        viewModelScope.launch {
            repository.getCities().collect {
                _citiesData.value = it
            }
        }
    }

    fun deleteCity(entity: CitiesEntity) {
        viewModelScope.launch {
            repository.deleteCity(entity)
        }
    }
}