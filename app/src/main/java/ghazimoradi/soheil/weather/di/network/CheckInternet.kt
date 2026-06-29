package ghazimoradi.soheil.weather.di.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ghazimoradi.soheil.weather.utils.network.CheckInternetConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckInternet {

    @Provides
    @Singleton
    fun provideCM(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideCheckInternet(
        connectivityManager: ConnectivityManager
    ): CheckInternetConnection = CheckInternetConnection(connectivityManager)
}