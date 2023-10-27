package com.tridhya.basesetupnew.Service

import com.tridhya.basesetupnew.Model.request.LoginRequest
import com.tridhya.basesetupnew.Model.request.RegisterationRequest
import com.tridhya.basesetupnew.Model.response.Register.RegisterResponse
import com.tridhya.basesetupnew.Model.response.SignIn.SigninResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {


    @POST("login")
    suspend fun signin(
        @Body loginRequest: LoginRequest
    ): Response<SigninResponse>


    @POST("registration")
    suspend fun register(
        @Body registerationRequest: RegisterationRequest
    ): Response<RegisterResponse>
}