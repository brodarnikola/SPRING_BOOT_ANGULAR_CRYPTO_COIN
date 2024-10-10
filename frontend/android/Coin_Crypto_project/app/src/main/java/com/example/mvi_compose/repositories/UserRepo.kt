package com.example.mvi_compose.repositories

import com.example.mvi_compose.network.NetworkResult
import com.example.mvi_compose.network.data.UserResponse

interface UserRepo {

    suspend fun getUsers(): NetworkResult<List<UserResponse>>
    suspend fun getUserById(userId: Long): NetworkResult<UserResponse>
}