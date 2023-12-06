package com.tridhya.chatsta.repository.Register

import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.tridhya.chatsta.Service.ApiClient
import com.tridhya.chatsta.Service.ApiState
import com.tridhya.chatsta.Service.NetworkConstants
import com.tridhya.chatsta.Service.ResponseState
import com.tridhya.chatsta.Model.request.RegisterationRequest
import com.tridhya.chatsta.utils.Constant
import com.tridhya.chatsta.utils.PrefUtils
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class RegisterRepository @Inject constructor(@Named(Constant.GSON) private val gson: Gson,
                                             @Named(Constant.API_CLIENT) private val apiClient: ApiClient,
                                             @Named(Constant.SHARED_COMMON) private val prefUtils: PrefUtils,) {
    suspend fun userRegister(
        parent: View?,
        isSuccessMessageShow: Boolean,
        isFailureMessageShow: Boolean,
        registerationRequest : RegisterationRequest
    ): ApiState {
        val responseData: ResponseState?
        if (Constant.isNetWork(parent!!.context)) {
            val response = apiClient.register(registerationRequest=registerationRequest)
            val responseBody = response.body()
            Log.d("TAG", "userRegister: "+response.code()+"\n message"+response.message())
            val responseMessage = response.message() ?: NetworkConstants.ErrorMsg.SOMETHING_WENT_WRONG
            responseData =
                ResponseState(
                    apiStatus = response.code(),
                    message = response.message(),
                    response = response as Response<Any>,
                    responseBody = responseBody,
                    parentView = parent,
                    isFailureMessageShow = isFailureMessageShow,
                    isSuccessMessageShow = isSuccessMessageShow

                )

        }else
            responseData =
                ResponseState(
                    parentView = parent,
                    isNetworkAvailable = false
                )
        return NetworkConstants.getApiStateResponseStatus(

            responseData, prefUtils
        )
    }

}