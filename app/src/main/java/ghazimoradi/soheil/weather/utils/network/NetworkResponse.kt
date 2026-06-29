package ghazimoradi.soheil.weather.utils.network

import com.google.gson.Gson
import ghazimoradi.soheil.weather.data.models.error.ResponseErrors
import retrofit2.Response

open class NetworkResponse<T>(private val response: Response<T>) {

    open fun generateResponse(): NetworkRequest<T> {
        return when {
            response.code() == 401 -> {
                var errorMessage = ""
                if (response.errorBody() != null) {
                    val errorResponse = Gson().fromJson(response.errorBody()?.charStream(),
                        ResponseErrors::class.java)
                    errorMessage = errorResponse.message.toString()
                }
                NetworkRequest.Error(errorMessage)
            }

            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}