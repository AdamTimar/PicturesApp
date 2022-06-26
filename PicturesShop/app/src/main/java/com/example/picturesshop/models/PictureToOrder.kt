package com.example.picturesshop.models

data class PictureToOrder(
    val pictureId: Int,
    val pictureName: String,
    val dimension: String,
    val dimensionId: Int,
    var quantity: Int,
    var price: Float,
    val imageUrl: String
)
