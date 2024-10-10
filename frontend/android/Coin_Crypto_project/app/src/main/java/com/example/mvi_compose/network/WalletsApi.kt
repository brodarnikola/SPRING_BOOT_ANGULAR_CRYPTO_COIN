package com.example.mvi_compose.network

import com.example.mvi_compose.network.data.ExchangeRatesResponse
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.network.data.WalletResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface WalletsApi {

    @GET("wallets")
    suspend fun getWallets(@Query("idUser") idUser: Int): Response<List<WalletResponse>>

    @GET("wallets/details")
    suspend fun getWalletInfoByUserId( @Query("idUser") idUser: Int ): Response<WalletResponse>
}