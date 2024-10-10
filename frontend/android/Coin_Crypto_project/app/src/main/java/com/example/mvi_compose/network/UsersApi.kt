package com.example.mvi_compose.network

import com.example.mvi_compose.network.data.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton


@Singleton
interface UsersApi {
    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserResponse>
}