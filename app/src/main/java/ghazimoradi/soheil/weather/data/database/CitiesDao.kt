package ghazimoradi.soheil.weather.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity
import ghazimoradi.soheil.weather.utils.CITIES_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {
    @Insert(onConflict = REPLACE)
    suspend fun saveCity(entity: CitiesEntity)

    @Delete
    suspend fun deleteCity(entity: CitiesEntity)

    @Query("Select * FROM $CITIES_TABLE")
    suspend fun loadCities(): Flow<List<CitiesEntity>>
}