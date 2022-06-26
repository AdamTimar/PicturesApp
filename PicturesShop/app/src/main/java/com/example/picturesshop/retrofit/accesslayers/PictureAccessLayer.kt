package com.example.picturesshop.retrofit.accesslayers

import com.example.picturesshop.retrofit.Proxy
import com.example.picturesshop.retrofit.models.*
import com.example.picturesshop.utils.ErrorMessageHandler
import io.reactivex.Observable
import io.reactivex.Single

object PictureAccessLayer {
    fun getPicturesObservable(token: String): Single<List<PictureResult>> {
        return Single.create { emitter ->
            try {
                val response = Proxy.getPictures(token)

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

    fun getPictureByIdObservable(token: String, id: Int): Single<PictureResult> {
        return Single.create { emitter ->
            val response = Proxy.getPictureById(token, id)

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

    fun getPictureContentsObservable(token: String): Single<List<ContentResult>> {
        return Single.create { emitter ->
            val response = Proxy.getPictureContents(token)

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

    fun getPictureSizesObservable(token: String): Single<List<SizeResult>> {
        return Single.create { emitter ->
            val response = Proxy.getPictureSizes(token)

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

    fun addOrderObservable(token: String, addOrderModel: AddOrderModel): Single<String> {
        return Single.create { emitter ->
            try {
                val response = Proxy.addOrder(addOrderModel, token)

                if (!response.isSuccessful) {
                    emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
                } else {
                    val result = response.body()!!.result
                    if (result != null) {
                        emitter.onSuccess(result)
                    }
                }
            }
            catch(ex: Exception){
                if (!emitter.isDisposed) {
                    emitter.onError(ex);
                }
            }

        }
    }

    fun addPictureObservable(token: String, addPictureModel: AddPictureModel): Single<Any> {
        return Single.create { emitter ->
            try {
                val response = Proxy.addPicture(addPictureModel, token)

                if (!response.isSuccessful) {
                    emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
                } else {
                    val result = response.body()!!.result
                    if (result != null) {
                        emitter.onSuccess(result)
                    }
                }
            }
            catch(ex: Exception){
                if (!emitter.isDisposed) {
                    emitter.onError(ex);
                }
            }

        }
    }

    fun deletePictureObservable(id: Int, token: String): Single<String> {
        return Single.create { emitter ->
            try {
                val response = Proxy.deletePicture(id, token)

                if (!response.isSuccessful) {
                    emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
                } else {
                    val result = response.body()!!.result
                    if (result != null) {
                        emitter.onSuccess(result)
                    }
                }
            }
            catch(ex: Exception){
                if (!emitter.isDisposed) {
                    emitter.onError(ex);
                }
            }

        }
    }

    fun getOrdersObservable(token: String): Single<List<UsersOrder>> {
        return Single.create { emitter ->
            try {
                val response = Proxy.getOrders(token)

                if (!response.isSuccessful) {
                    emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
                } else {
                    val result = response.body()!!.result
                    if (result != null) {
                        emitter.onSuccess(result)
                    }
                }
            }
            catch(ex: Exception){
                if (!emitter.isDisposed) {
                    emitter.onError(ex);
                }
            }

        }
    }

}