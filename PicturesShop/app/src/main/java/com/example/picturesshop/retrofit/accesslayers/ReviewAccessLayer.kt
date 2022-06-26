package com.example.picturesshop.retrofit.accesslayers

import com.example.picturesshop.retrofit.Proxy
import com.example.picturesshop.retrofit.models.AddReviewModel
import com.example.picturesshop.retrofit.models.ReviewResult
import com.example.picturesshop.utils.ErrorMessageHandler
import io.reactivex.Single

object ReviewAccessLayer {
    fun getPictureReviewsObservable(token: String, id: Int): Single<List<ReviewResult>> {
        return Single.create { emitter ->
            val response = Proxy.getPictureReviews(token, id)

            if (!response.isSuccessful) {
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            } else {
                val result = response.body()!!.result
                if (result != null) {
                    emitter.onSuccess(result)
                }
            }
        }
    }

    fun getDeleteReviewByIdObservable(token: String, id: Int): Single<String> {
        return Single.create { emitter ->
            try {
                val response = Proxy.deleteReviewById(id, token)

                if (!response.isSuccessful) {
                    emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
                } else {
                    val result = response.body()!!.result
                    if (result != null) {
                        emitter.onSuccess(result)
                    }
                }
            } catch (ex: Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(ex);
                }
            }
        }
    }

    fun getAddReviewObservable(
        token: String,
        addReviewModel: AddReviewModel
    ): Single<ReviewResult> {
        return Single.create { emitter ->
            try {
                val response = Proxy.addReview(addReviewModel, token)

                if (!response.isSuccessful) {
                    emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
                } else {
                    val result = response.body()!!.result
                    if (result != null) {
                        emitter.onSuccess(result)
                    }
                }
            } catch (ex: Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(ex);
                }
            }
        }
    }
}