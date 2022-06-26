package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class SizeResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("price")
    val price: Float
)
