package com.example.mvi_compose.repositories

import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.network.data.UserResponse

interface ExchangeRatesRepo {

    suspend fun getExchangeRates(): NetworkResult<List<ExchangeRatesResponse>>
}