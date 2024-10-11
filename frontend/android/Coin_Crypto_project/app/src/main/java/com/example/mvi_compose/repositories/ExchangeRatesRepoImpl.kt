package com.example.mvi_compose.repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mvi_compose.di.IODispatcher
import com.example.mvi_compose.network.ExchangeRatesApi
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.util.AppConstants.Companion.REST_API_CALL
import com.example.mvi_compose.util.handleNetworkRequest
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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

            Log.d(REST_API_CALL, "start popular ExchangeRates")

            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val twoYearsAgo = LocalDate.now().minusYears(2).format(DateTimeFormatter.ISO_DATE)

            val networkResult = handleNetworkRequest(
                apiCall = {
                    exchangeRatesApi.getExchangeRates(
                        dateFrom = twoYearsAgo,
                        dateTo = today
                    )
                },
                moshi = moshi
            )

            if (networkResult is NetworkResult.Success) {
                Log.d(REST_API_CALL, "get users success")
            }

            return@withContext networkResult
        }

}