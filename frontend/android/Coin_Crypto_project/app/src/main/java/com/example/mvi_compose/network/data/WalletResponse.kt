package com.example.mvi_compose.network.data

data class WalletResponse(
    val id: Int,
    val idUser: Int,
    val coinToken: String,
    val countTimeStamp: String  )