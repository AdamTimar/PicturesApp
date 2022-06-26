package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class PictureResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("discount")
    val discount: Int
)
