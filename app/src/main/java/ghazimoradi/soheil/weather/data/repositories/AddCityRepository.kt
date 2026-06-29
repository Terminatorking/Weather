package ghazimoradi.soheil.weather.data.repositories

import ghazimoradi.soheil.weather.data.network.ApiServices
import ghazimoradi.soheil.weather.utils.other.CITIES_LIMIT
import javax.inject.Inject

class AddCityRepository @Inject constructor(private val api: ApiServices) {
    //API
    suspend fun getCitiesList(q: String) = api.getCitiesList(q, CITIES_LIMIT)
}