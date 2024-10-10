package com.example.mvi_compose.repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mvi_compose.di.IODispatcher
import com.example.mvi_compose.network.ApiError
import com.example.mvi_compose.network.ExchangeRatesApi
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.util.AppConstants.Companion.REST_API_CALL
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRatesRepoImpl @Inject constructor(
     private val exchangeRatesApi: ExchangeRatesApi,
     private val moshi: Moshi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ExchangeRatesRepo {
 

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getExchangeRates(): NetworkResult<List<ExchangeRatesResponse>> = withContext(ioDispatcher) {

        val networkResult = handleNetworkRequest {
            Log.d(REST_API_CALL,"start popular ExchangeRates")

            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val twoYearsAgo = LocalDate.now().minusYears(2).format(DateTimeFormatter.ISO_DATE)

            exchangeRatesApi.getExchangeRates(dateFrom = twoYearsAgo, dateTo = today )
        }

        if( networkResult is NetworkResult.Success ) {
            Log.d(REST_API_CALL,"get ExchangeRates success")
        }

        return@withContext networkResult
    } 
    
    private suspend fun <T : Any> handleNetworkRequest(apiCall: suspend () -> Response<T>): NetworkResult<T> {
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
                        Log.e("Error","handleNetworkRequest error: ${e.localizedMessage}")
                    }
                }
                NetworkResult.Error(
                    code = response.code(),
                    message = response.message(),
                    apiError = apiError
                )
            }
        } catch (e: HttpException) {
            Log.e("NETWORK_HTTP_ERROR","Network request error - HttpException: ${e.localizedMessage}")
            NetworkResult.Error(
                code = e.code(),
                message = e.message(),
                apiError = null
            )
        } catch (e: IOException) {
            Log.e("NETWORK_IOEXCEPTION_ERROR","Network request error - IOException: ${e.localizedMessage}")
            NetworkResult.Exception(e)
        }
    }

}