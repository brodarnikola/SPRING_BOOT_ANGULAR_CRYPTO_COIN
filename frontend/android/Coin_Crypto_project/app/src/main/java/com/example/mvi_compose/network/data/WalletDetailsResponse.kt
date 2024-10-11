package com.example.mvi_compose.network.data

data class WalletDetailsResponse(
    val wallets: List<WalletResponse>,
    val totalWorth: Int,
    val lastPurchase: WalletResponse   )