package com.tridhya.basesetupnew.Service

import android.util.Log
import com.tridhya.basesetupnew.Model.request.LoginRequest
import com.tridhya.basesetupnew.Model.request.RegisterationRequest

import com.tridhya.basesetupnew.Model.response.Register.RegisterResponse
import com.tridhya.basesetupnew.Model.response.SignIn.SigninResponse

import com.tridhya.basesetupnew.utils.Constant.SERVICE_WITH_GSON_SIGNIN
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class ApiClient @Inject constructor(
    @Named(SERVICE_WITH_GSON_SIGNIN) private val serviceForSignIn: ApiInterface,

) {

    suspend fun signin(loginRequest: LoginRequest):Response<SigninResponse>{
        return serviceForSignIn.signin(loginRequest)
    }
    suspend fun register(registerationRequest : RegisterationRequest):Response<RegisterResponse>{
        return serviceForSignIn.register(registerationRequest = registerationRequest)
    }
}