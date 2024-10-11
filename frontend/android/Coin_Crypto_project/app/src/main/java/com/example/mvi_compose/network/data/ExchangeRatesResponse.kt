package com.example.mvi_compose.network.data

data class ExchangeRatesResponse(
    val id: Int,
    val excRateDate: String,
    val excRateEur: Float,
    val excRateUsd: Float,
    val excRateGbp: Float  )