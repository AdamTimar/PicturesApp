package com.example.picturesshop.models

data class Order(
    val pictures: MutableList<PictureToOrder>,
    val orderDate: String
)
