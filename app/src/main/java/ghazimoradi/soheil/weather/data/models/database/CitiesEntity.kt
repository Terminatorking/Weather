package ghazimoradi.soheil.weather.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ghazimoradi.soheil.weather.utils.other.CITIES_TABLE

@Entity(tableName = CITIES_TABLE)
data class CitiesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String? = null,
    var lat: Double? = null,
    var lon: Double? = null
)