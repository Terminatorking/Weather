package ghazimoradi.soheil.weather.di.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ghazimoradi.soheil.weather.BuildConfig.DEBUG
import ghazimoradi.soheil.weather.data.network.ApiServices
import ghazimoradi.soheil.weather.utils.API_KEY
import ghazimoradi.soheil.weather.utils.APPID
import ghazimoradi.soheil.weather.utils.BASE_URL
import ghazimoradi.soheil.weather.utils.CONNECTION_TIME
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideTimeout() = CONNECTION_TIME

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = if (DEBUG) BODY else NONE
    }

    @Provides
    @Singleton
    fun provideClient(
        timeout: Long,
        interceptor: HttpLoggingInterceptor,
    ) = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val url = chain
                .request()
                .url.newBuilder()
                .addQueryParameter(APPID, API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }.also { client ->
            client.addInterceptor(interceptor)
        }
        .writeTimeout(timeout, SECONDS)
        .readTimeout(timeout, SECONDS)
        .connectTimeout(timeout, SECONDS)
        .retryOnConnectionFailure(true)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)
}