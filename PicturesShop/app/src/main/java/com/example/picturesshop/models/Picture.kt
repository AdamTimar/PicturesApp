package com.example.picturesshop.models

data class Picture(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val content: String,
    val discount: Int
)
