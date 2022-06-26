package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class VerificationModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("validationKey")
    val validationKey: String
)
