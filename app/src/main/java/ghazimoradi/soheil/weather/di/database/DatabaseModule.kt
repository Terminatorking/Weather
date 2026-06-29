package ghazimoradi.soheil.weather.di.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ghazimoradi.soheil.weather.data.database.CitiesDataBase
import ghazimoradi.soheil.weather.utils.other.CITIES_DATABASE
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CitiesDataBase {
        val builder = Room.databaseBuilder(
            context,
            CitiesDataBase::class.java,
            CITIES_DATABASE
        )

        builder.allowMainThreadQueries()
        builder.fallbackToDestructiveMigration(false)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideDao(database: CitiesDataBase) = database.dao()
}