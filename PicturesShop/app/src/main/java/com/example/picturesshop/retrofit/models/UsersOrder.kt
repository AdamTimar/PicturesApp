package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class UsersOrder(
    @SerializedName("userName")
    var userName: String,
    @SerializedName("pictures")
    var pictures: List<OrderedPicture>,
    @SerializedName("orderDate")
    var orderDate: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("price")
    var price: Float,
    @SerializedName("userId")
    var userId: Int

)
