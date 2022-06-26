package com.example.picturesshop.retrofit

import com.example.picturesshop.retrofit.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Proxy {

    private const val BASE_URL = "https://8988-86-123-132-186.ngrok.io/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    private val service: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }


    fun login(email: String, password:String) = service.login(LoginCredentials(email, password)).execute()
    fun registration(email: String, password:String, firstName: String, lastName: String, birthDate: String) = service.registration(
        RegistrationModel(email, password, firstName, lastName, birthDate)
    ).execute()
    fun verification(email: String, validationKey: String) = service.verification(VerificationModel(email, validationKey)).execute()
    fun sendPasswordChangeKey(email: String) = service.sendPasswordChangeKey(SendPasswordChangeModel( email)).execute()
    fun validatePasswordChangeKey(email: String, validationKey: String) = service.validatePasswordChangeKey(VerificationModel(email, validationKey)).execute()
    fun changePassword(email: String, newPassword: String) = service.changePassword(LoginCredentials(email, newPassword)).execute()
    fun getPictures(token: String) = service.getPictures(token).execute()
    fun getPictureById(token: String, id: Int) = service.getPictureById(token, id).execute()
    fun getPictureContents(token: String) = service.getPictureContents(token).execute()
    fun getPictureSizes(token: String) = service.getPictureSizes(token).execute()
    fun getPictureReviews(token: String, id: Int) = service.getPictureReviews(id, token).execute()
    fun deleteReviewById(id: Int, token: String) = service.deleteReviewById(id, token).execute()
    fun addReview(addReviewModel: AddReviewModel, token: String) = service.addReview(addReviewModel, token).execute()
    fun addOrder(addOrderModel: AddOrderModel, token: String) = service.addOrder(addOrderModel, token).execute()
    fun addPicture(addPictureModel: AddPictureModel, token: String) = service.addPicture(addPictureModel, token).execute()
    fun deletePicture(id: Int, token: String) = service.deletePicture(id, token).execute()
    fun getOrders(token: String) = service.getOrders(token).execute()
}
