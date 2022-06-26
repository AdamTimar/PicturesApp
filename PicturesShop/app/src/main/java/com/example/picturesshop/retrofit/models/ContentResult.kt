package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class ContentResult(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
)