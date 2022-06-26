package com.example.picturesshop.retrofit.accesslayers

import android.util.Log
import com.example.picturesshop.retrofit.Proxy
import com.example.picturesshop.retrofit.models.LoginResult
import com.example.picturesshop.utils.ErrorMessageHandler
import io.reactivex.Single

object UserAccessLayer {

    fun getLoginObservable(userName: String, password: String): Single<LoginResult> {
        return Single.create { emitter ->
            val response = Proxy.login(userName,password)

            if (!response.isSuccessful){
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            }
            else {
                val result = response.body()!!.result
                if( result != null) {
                    emitter.onSuccess(result)
                }
            }
        }
    }

    fun getRegistrationObservable(email: String, password: String, firstName: String, lastName: String, birthDate: String): Single<Boolean> {
        return Single.create { emitter ->
            val response = Proxy.registration(email, password, firstName, lastName, birthDate)

            if (!response.isSuccessful){
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            }
            else {
                val result = response.body()!!.result
                if( result != null) {
                    emitter.onSuccess(true)
                }
            }
        }
    }

    fun getVerificationObservable(email: String, validationKey: String): Single<Boolean> {
        return Single.create { emitter ->
            val response = Proxy.verification(email, validationKey)

            if (!response.isSuccessful){
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            }
            else {
                val result = response.body()!!.result
                if( result != null) {
                    emitter.onSuccess(true)
                }
            }
        }
    }

    fun getSendPasswordChangeObservable(email: String): Single<Boolean> {
        return Single.create { emitter ->
            val response = Proxy.sendPasswordChangeKey(email)

            if (!response.isSuccessful){
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            }
            else {
                val result = response.body()!!.result
                if( result != null) {
                    emitter.onSuccess(true)
                }
            }
        }
    }

    fun getValidatePasswordChangeObservable(email: String, validationKey: String): Single<Boolean> {
        return Single.create { emitter ->
            val response = Proxy.validatePasswordChangeKey(email, validationKey)

            if (!response.isSuccessful){
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            }
            else {
                val result = response.body()!!.result
                if( result != null) {
                    emitter.onSuccess(true)
                }
            }
        }
    }

    fun getPasswordChangeObservable(email: String, newPassword: String): Single<Boolean> {
        return Single.create { emitter ->
            val response = Proxy.changePassword(email, newPassword)

            if (!response.isSuccessful){
                emitter.onError(Exception(ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)))
            }
            else {
                val result = response.body()!!.result
                if( result != null) {
                    emitter.onSuccess(true)
                }
            }
        }
    }
}