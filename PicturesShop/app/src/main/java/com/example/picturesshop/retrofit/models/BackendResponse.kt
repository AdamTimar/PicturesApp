package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class BackendResponse<T>(
    @SerializedName("result")
    val result: T?,
    @SerializedName("code")
    val code: Int,
    @SerializedName("error")
    val error: String?
)
