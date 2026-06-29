package ghazimoradi.soheil.weather.di.fragment

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import ghazimoradi.soheil.weather.data.models.database.CitiesEntity

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {
    @Provides
    fun cityEntity() = CitiesEntity()
}