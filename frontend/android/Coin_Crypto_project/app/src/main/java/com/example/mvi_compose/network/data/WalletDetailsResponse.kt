package com.example.mvi_compose.network.data

data class WalletDetailsResponse(
    val wallets: List<WalletResponse>,
    val totalWorth: Int,
    val lastPurchase: WalletResponse   )

//{
//    "id": 59,
//    "idUser": 1,
//    "coinToken": "b76b658d-ba03-4cb8-aced-e11bb08a7cca",
//    "countTimeStamp": "2024-09-04T20:59:21.281159"
//},