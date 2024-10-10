package com.example.mvi_compose.repositories

import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.network.data.WalletResponse

interface WalletRepo {

    suspend fun getWallets(userId: Int): NetworkResult<List<WalletResponse>>
    suspend fun getWalletInfoByUserId(userId: Int): NetworkResult<WalletResponse>
}