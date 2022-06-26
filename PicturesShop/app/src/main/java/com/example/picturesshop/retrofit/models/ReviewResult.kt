package com.example.picturesshop.retrofit.models

import com.google.gson.annotations.SerializedName

data class ReviewResult(
    @SerializedName("id")
    var id: Int,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("userName")
    var userName: String,
    @SerializedName("qualityLevel")
    var quality: Int,
    @SerializedName("comment")
    var comment: String,
    @SerializedName("pictureId")
    var pictureId: Int
)
