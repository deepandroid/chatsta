package com.tridhya.basesetupnew.Service

import com.tridhya.basesetupnew.request.LoginRequest
import com.tridhya.basesetupnew.request.RegisterationRequest
import com.tridhya.basesetupnew.response.LoginResponse
import com.tridhya.basesetupnew.response.Register.RegisterResponse
import com.tridhya.basesetupnew.response.SignIn.SigninResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("Login/{username}/{password}/{appName}/{deviceId}")
    suspend fun users(
        @Header("Authorization") auth: String,
        @Path("username") username: String,
        @Path("password") password: String,
        @Path("appName") appName: String,
        @Path("deviceId") deviceId: String,
    ): Response<List<LoginResponse>>


    @POST("login")
    suspend fun signin(
        @Body loginRequest: LoginRequest
    ): Response<SigninResponse>


    @POST("registration")
    suspend fun register(
        @Body registerationRequest: RegisterationRequest
    ): Response<RegisterResponse>
}