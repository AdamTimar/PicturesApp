package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class AddOrderModel (
    @SerializedName("orders")
    val orders: List<OrderModel>,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("location")
    val location: String
)