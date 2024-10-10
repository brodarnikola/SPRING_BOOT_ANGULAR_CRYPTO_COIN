package com.example.mvi_compose.network.data

data class WalletResponse(
    val id: Int,
    val idUser: Int,
    val coinToken: String,
    val countTimeStamp: String  )

//{
//    "id": 59,
//    "idUser": 1,
//    "coinToken": "b76b658d-ba03-4cb8-aced-e11bb08a7cca",
//    "countTimeStamp": "2024-09-04T20:59:21.281159"
//},