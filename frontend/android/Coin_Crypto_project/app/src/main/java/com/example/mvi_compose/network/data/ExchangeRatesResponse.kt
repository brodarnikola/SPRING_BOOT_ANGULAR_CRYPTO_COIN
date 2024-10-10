package com.example.mvi_compose.network.data

data class ExchangeRatesResponse(
    val id: Int,
    val excRateDate: String,
    val excRateEur: Float,
    val excRateUsd: Float,
    val excRateGbp: Float  )

//
//"id": 483,
//"excRateDate": "2022-10-10",
//"excRateEur": 1.2228,
//"excRateUsd": 1.3538,
//"excRateGbp": 0.2727