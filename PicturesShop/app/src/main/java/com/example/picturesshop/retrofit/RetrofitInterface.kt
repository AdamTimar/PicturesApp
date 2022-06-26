package com.example.picturesshop.retrofit


import com.example.picturesshop.retrofit.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @POST("api/users/login")
    fun login(@Body loginCredentials: LoginCredentials): Call<BackendResponse<LoginResult>>

    @POST("api/users/sendRegistrationKey")
    fun registration(@Body registrationModel: RegistrationModel): Call<BackendResponse<Any>>

    @POST("api/users/registration")
    fun verification(@Body verificationModel: VerificationModel): Call<BackendResponse<Any>>

    @POST("api/users/sendChangePasswordKey")
    fun sendPasswordChangeKey(@Body sendPasswordChangeModel: SendPasswordChangeModel): Call<BackendResponse<Any>>

    @POST("api/users/validatePasswordChangeKey")
    fun validatePasswordChangeKey(@Body sendPasswordChangeModel: VerificationModel): Call<BackendResponse<String>>

    @PUT("api/users/changePassword")
    fun changePassword(@Body sendPasswordChangeModel: LoginCredentials): Call<BackendResponse<String>>

    @GET("api/Pictures")
    fun getPictures(@Header ("Authorization") token: String): Call<BackendResponse<List<PictureResult>>>

    @GET("api/Pictures/{id}")
    fun getPictureById(@Header ("Authorization") token: String, @Path("id") id:Int): Call<BackendResponse<PictureResult>>

    @GET("api/Pictures/Contents")
    fun getPictureContents(@Header ("Authorization") token: String): Call<BackendResponse<List<ContentResult>>>

    @GET("api/Pictures/Sizes")
    fun getPictureSizes(@Header ("Authorization") token: String): Call<BackendResponse<List<SizeResult>>>

    @GET("api/Reviews/PictureReviews{pictureId}")
    fun getPictureReviews(@Path("pictureId") pictureId: Int, @Header("Authorization") token:String): Call<BackendResponse<List<ReviewResult>>>

    @DELETE("api/Reviews/{id}")
    fun deleteReviewById(@Path("id") id:Int, @Header("Authorization") token: String): Call <BackendResponse<String>>

    @POST("api/Reviews")
    fun addReview(@Body reviews: AddReviewModel, @Header("Authorization") token: String): Call <BackendResponse<ReviewResult>>

    @POST("api/Orders")
    fun addOrder(@Body reviews: AddOrderModel, @Header("Authorization") token: String): Call <BackendResponse<String>>

    @POST("api/Pictures")
    fun addPicture(@Body picture: AddPictureModel, @Header("Authorization") token: String): Call <BackendResponse<Any>>

    @DELETE("api/Pictures/{id}")
    fun deletePicture(@Path("id") id: Int, @Header("Authorization") token: String): Call <BackendResponse<String>>

    @GET("api/Orders")
    fun getOrders(@Header("Authorization") token: String): Call <BackendResponse<List<UsersOrder>>>

}