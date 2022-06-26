package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class SendPasswordChangeModel(
    @SerializedName("email")
    val email: String
)
