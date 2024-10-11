package com.example.mvi_compose.util

import android.util.Log
import com.example.mvi_compose.network.ApiError
import com.example.mvi_compose.network.NetworkResult
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> handleNetworkRequest(
    apiCall: suspend () -> Response<T>,
    moshi: Moshi
): NetworkResult<T> {
    return try {
        val response: Response<T> = apiCall.invoke()

        if (response.isSuccessful && response.code() == 200 && response.body() != null) {
            NetworkResult.Success(response.body()!!)
        } else {
            val errorBody = response.errorBody()?.string()
            var apiError: ApiError? = null
            if (errorBody != null) {
                try {
                    val adapter = moshi.adapter(ApiError::class.java)
                    apiError = adapter.fromJson(errorBody)
                } catch (e: Exception) {
                    Log.e("Error", "handleNetworkRequest error: ${e.localizedMessage}")
                }
            }
            NetworkResult.Error(
                code = response.code(),
                message = response.message(),
                apiError = apiError
            )
        }
    } catch (e: HttpException) {
        Log.e("NETWORK_HTTP_ERROR", "Network request error - HttpException: ${e.localizedMessage}")
        NetworkResult.Error(
            code = e.code(),
            message = e.message(),
            apiError = null
        )
    } catch (e: IOException) {
        Log.e("NETWORK_IOEXCEPTION_ERROR", "Network request error - IOException: ${e.localizedMessage}")
        NetworkResult.Exception(e)
    }
}