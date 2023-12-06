package com.tridhya.chatsta.repository.Signin

import android.view.View
import com.tridhya.chatsta.Service.ApiClient
import com.tridhya.chatsta.Service.ApiState
import com.tridhya.chatsta.Service.NetworkConstants
import com.tridhya.chatsta.Service.NetworkConstants.getApiStateResponseStatus
import com.tridhya.chatsta.Service.ResponseState
import com.tridhya.chatsta.Model.request.LoginRequest
import com.tridhya.chatsta.utils.Constant
import com.tridhya.chatsta.utils.PrefUtils
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class SigninRepository @Inject constructor(
    @Named(Constant.API_CLIENT) private val apiClient: ApiClient,
    @Named(Constant.SHARED_COMMON) private val prefUtils: PrefUtils,
) {
    suspend fun userSignin(
        parent: View?,

        isSuccessMessageShow: Boolean,
        isFailureMessageShow: Boolean,
        loginRequest: LoginRequest
    ): ApiState {

        val responseData: ResponseState?
        if (Constant.isNetWork(parent!!.context)) {
            val response = apiClient.signin(loginRequest)
            val responseBody = response.body()

            val responseMessage = response.message() ?: NetworkConstants.ErrorMsg.SOMETHING_WENT_WRONG
            responseData =
                ResponseState(
                    apiStatus = response.code(),
                    message = response.body()?.message,
                    response = response as Response<Any>,
                    responseBody = responseBody,
                    parentView = parent,
                    isFailureMessageShow = isFailureMessageShow,
                    isSuccessMessageShow = isSuccessMessageShow

                )

        } else
            responseData =
                ResponseState(
                    parentView = parent,
                    isNetworkAvailable = false
                )
        return getApiStateResponseStatus(

            responseData, prefUtils
        )
    }

}