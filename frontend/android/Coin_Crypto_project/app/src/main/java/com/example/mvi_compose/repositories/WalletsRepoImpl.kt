package com.example.mvi_compose.repositories

import android.util.Log
import com.example.mvi_compose.di.IODispatcher
import com.example.mvi_compose.network.ApiError
import com.example.mvi_compose.network.UsersApi
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.WalletsApi
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.network.data.WalletDetailsResponse
import com.example.mvi_compose.network.data.WalletResponse
import com.example.mvi_compose.util.AppConstants.Companion.REST_API_CALL
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletsRepoImpl @Inject constructor(
     private val walletsApi: WalletsApi,
     private val moshi: Moshi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : WalletRepo {

    override suspend fun getWallets(userId: Int): NetworkResult<List<WalletResponse>> = withContext(ioDispatcher) {
        val networkResult = handleNetworkRequest {
            Log.d(REST_API_CALL,"start popular coin crypto")
            walletsApi.getWallets(userId)
        }

        if( networkResult is NetworkResult.Success ) {
            Log.d(REST_API_CALL,"get wallets success")
        }

        return@withContext networkResult
    }

    override suspend fun getWalletInfoByUserId(userId: Int): NetworkResult<WalletDetailsResponse> = withContext(ioDispatcher) {
        val networkResult = handleNetworkRequest {
            Log.d(REST_API_CALL,"start popular coin crypto")
            walletsApi.getWalletInfoByUserId(userId)
        }

        if( networkResult is NetworkResult.Success ) {
            Log.d(REST_API_CALL,"get wallet info by user id success ${networkResult.data}")
        }

        return@withContext networkResult
    }

    private suspend fun <T : Any> handleNetworkRequest(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response: Response<T> = apiCall.invoke()

            if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                var apiError: ApiError? = null
                if (errorBody != null) {
                    try {
                        val adapter = moshi.adapter(ApiError::class.java)
                        apiError = adapter.fromJson(errorBody)
                    } catch (e: Exception) {
                        Log.e("Error","handleNetworkRequest error: ${e.localizedMessage}")
                    }
                }
                NetworkResult.Error(
                    code = response.code(),
                    message = response.message(),
                    apiError = apiError
                )
            }
        } catch (e: HttpException) {
            Log.e("NETWORK_HTTP_ERROR","Network request error - HttpException: ${e.localizedMessage}")
            NetworkResult.Error(
                code = e.code(),
                message = e.message(),
                apiError = null
            )
        } catch (e: IOException) {
            Log.e("NETWORK_IOEXCEPTION_ERROR","Network request error - IOException: ${e.localizedMessage}")
            NetworkResult.Exception(e)
        }
    }

}