package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class AddReviewModel(
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("comment")
    var comment: String,
    @SerializedName("qualityLevel")
    var qualityLevel: Int,
    @SerializedName("pictureId")
    var pictureId: Int
)
