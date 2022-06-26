package com.example.picturesshop.models

data class Review(
    var user: String,
    var comment: String,
    var qualityLevel: Int,
    var id: Int,
    var userId: Int,
    var pictureId: Int
)
