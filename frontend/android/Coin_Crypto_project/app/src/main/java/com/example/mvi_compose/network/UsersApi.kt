package com.example.mvi_compose.network

import com.example.mvi_compose.network.data.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface UsersApi {
    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>
}