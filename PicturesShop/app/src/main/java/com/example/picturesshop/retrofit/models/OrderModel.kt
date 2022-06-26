package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class OrderModel(
    @SerializedName("pictureId")
    val pictureId: Int,
    @SerializedName("sizeId")
    val sizeId: Int,
    @SerializedName("quantity")
    val quantity: Int
)
