package com.example.mvi_compose.network

import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.network.data.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface ExchangeRatesApi {
    @GET("exchange-rates")
    suspend fun getExchangeRates( @Query("dateFrom") dateFrom: String,
                                  @Query("dateTo") dateTo: String ): Response<List<ExchangeRatesResponse>>
}