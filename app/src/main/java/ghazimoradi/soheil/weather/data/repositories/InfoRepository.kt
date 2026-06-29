package ghazimoradi.soheil.weather.data.repositories

import ghazimoradi.soheil.weather.data.network.ApiServices
import javax.inject.Inject

class InfoRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getPollution(lat: Double, lon: Double) = api.getPollution(lat, lon)
}