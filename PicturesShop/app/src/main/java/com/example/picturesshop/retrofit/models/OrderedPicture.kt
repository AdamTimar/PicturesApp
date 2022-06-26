package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class OrderedPicture(
    @SerializedName("pictureName")
    var pictureName: String,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("size")
    var size: String,
    @SerializedName("content")
    var content: String,
    @SerializedName("price")
    var price: Float,
    @SerializedName("imageUrl")
    var imageUrl: String
)
