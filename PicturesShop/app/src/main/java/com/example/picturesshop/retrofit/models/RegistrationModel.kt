package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class RegistrationModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("birthDate")
    val birthDate: String
)
