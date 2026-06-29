package ghazimoradi.soheil.weather.data.repositories

import ghazimoradi.soheil.weather.data.database.CitiesDao
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import javax.inject.Inject

class CitiesRepository @Inject constructor(private val dao: CitiesDao) {

    fun getCities() = dao.loadCities()
    suspend fun deleteCity(entity: CitiesEntity) = dao.deleteCity(entity)
}