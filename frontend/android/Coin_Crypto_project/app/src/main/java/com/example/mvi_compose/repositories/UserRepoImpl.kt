package com.example.mvi_compose.repositories

import android.util.Log
import com.example.mvi_compose.di.IODispatcher
import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.UsersApi
import com.example.mvi_compose.network.data.UserResponse
import com.example.mvi_compose.util.AppConstants.Companion.REST_API_CALL
import com.example.mvi_compose.util.handleNetworkRequest
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepoImpl @Inject constructor(
     private val usersApi: UsersApi,
     private val moshi: Moshi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepo {

    override suspend fun getUsers(): NetworkResult<List<UserResponse>> = withContext(ioDispatcher) {

        Log.d(REST_API_CALL,"start popular coin crypto")
        val networkResult = handleNetworkRequest(
            apiCall = { usersApi.getUsers() },
            moshi = moshi
        )

        if (networkResult is NetworkResult.Success) {
            Log.d(REST_API_CALL, "get users success")
        }

        return@withContext networkResult
    }

    override suspend fun getUserById(userId: Long): NetworkResult<UserResponse> = withContext(ioDispatcher) {

        Log.d(REST_API_CALL,"start popular coin crypto")
        val networkResult = handleNetworkRequest(
            apiCall = { usersApi.getUserById(userId) },
            moshi = moshi
        )

        if (networkResult is NetworkResult.Success) {
            Log.d(REST_API_CALL, "get users success")
        }

        return@withContext networkResult
    }

}