package ghazimoradi.soheil.weather.data.repositories

import ghazimoradi.soheil.weather.data.database.CitiesDao
import ghazimoradi.soheil.weather.data.network.ApiServices
import ghazimoradi.soheil.weather.utils.other.FA
import ghazimoradi.soheil.weather.utils.other.METRIC
import javax.inject.Inject

class MainRepository @Inject constructor(private val dao: CitiesDao, private val api: ApiServices) {
    //Database
    fun getCities() = dao.loadCities()

    //Api
    suspend fun getCurrentWeather(lat: Double, lon: Double) =
        api.getCurrentWeather(lat, lon, FA, METRIC)

    suspend fun getForecast(lat: Double, lon: Double) =
        api.getForecast(lat, lon, FA, METRIC)
}