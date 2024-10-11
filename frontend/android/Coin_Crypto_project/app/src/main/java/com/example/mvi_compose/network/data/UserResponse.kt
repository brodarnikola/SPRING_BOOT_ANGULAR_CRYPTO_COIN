package com.example.mvi_compose.network.data


data class UserResponse(
    val id: Int,
    val fullName: String,
    val address: String,
    val postal: String,
    val city: String,
    val coinValue: Int )