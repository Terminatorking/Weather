package ghazimoradi.soheil.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity

@Database(entities = [CitiesEntity::class], version = 1, exportSchema = false)
abstract class CitiesDataBase : RoomDatabase() {
    abstract fun dao(): CitiesDao
}