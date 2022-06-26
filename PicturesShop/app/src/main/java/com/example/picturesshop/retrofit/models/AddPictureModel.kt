package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class AddPictureModel(
    @SerializedName("name")
    var name: String,
    @SerializedName("imageUrl")
    var url: String,
    @SerializedName("contentTypeId")
    var contentId: Int
)
