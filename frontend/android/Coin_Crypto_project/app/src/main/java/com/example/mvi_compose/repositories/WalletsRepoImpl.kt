package com.example.mvi_compose.repositories

import android.util.Log
import com.example.mvi_compose.di.IODispatcher
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.util.handleNetworkRequest
import com.example.mvi_compose.network.WalletsApi
import com.example.mvi_compose.network.data.WalletDetailsResponse
import com.example.mvi_compose.network.data.WalletResponse
import com.example.mvi_compose.util.AppConstants.Companion.REST_API_CALL
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletsRepoImpl @Inject constructor(
     private val walletsApi: WalletsApi,
     private val moshi: Moshi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : WalletRepo {

    override suspend fun getWallets(userId: Int): NetworkResult<List<WalletResponse>> = withContext(ioDispatcher) {
        val networkResult = handleNetworkRequest(
            apiCall = { walletsApi.getWallets(userId) },
            moshi = moshi
        )

        if (networkResult is NetworkResult.Success) {
            Log.d(REST_API_CALL, "get wallets success")
        }

        return@withContext networkResult
    }

    override suspend fun getWalletInfoByUserId(userId: Int): NetworkResult<WalletDetailsResponse> = withContext(ioDispatcher) {

        val networkResult = handleNetworkRequest(
            apiCall = { walletsApi.getWalletInfoByUserId(userId) },
            moshi = moshi
        )

        if (networkResult is NetworkResult.Success) {
            Log.d(REST_API_CALL, "get wallet info by user id success ${networkResult.data}")
        }

        return@withContext networkResult
    }

}