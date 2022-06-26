package com.example.picturesshop.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.picturesshop.models.Picture
import com.example.picturesshop.models.PictureToOrder
import com.example.picturesshop.models.Review
import com.example.picturesshop.retrofit.models.Content
import com.example.picturesshop.retrofit.models.Size

class MainActivityViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var pictures : MutableList<Picture>

    lateinit var sizes : List<Size>

    lateinit var contents : List<Content>

    var order  = mutableListOf<PictureToOrder>()

    var reviews = mutableListOf<Review>()

}
