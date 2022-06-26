package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("birthDate")
    val birthDate: String
)
