package com.tridhya.basesetupnew.Service

import android.util.Log
import com.tridhya.basesetupnew.dao.UserDataDao
import com.tridhya.basesetupnew.request.LoginRequest
import com.tridhya.basesetupnew.request.RegisterationRequest
import com.tridhya.basesetupnew.response.LoginResponse
import com.tridhya.basesetupnew.response.Register.RegisterResponse
import com.tridhya.basesetupnew.response.SignIn.SigninResponse
import com.tridhya.basesetupnew.utils.Constant.APP_NAME
import com.tridhya.basesetupnew.utils.Constant.AUTHORIZATION_KEY
import com.tridhya.basesetupnew.utils.Constant.DEVICE_ID
import com.tridhya.basesetupnew.utils.Constant.SERVICE_FOR_LOGIN_GSON
import com.tridhya.basesetupnew.utils.Constant.SERVICE_WITH_GSON
import com.tridhya.basesetupnew.utils.Constant.SERVICE_WITH_GSON_SIGNIN
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class ApiClient @Inject constructor(
    @Named(SERVICE_WITH_GSON) private val service: ApiInterface,
    @Named(SERVICE_FOR_LOGIN_GSON) private val serviceForLogin: ApiInterface,
    @Named(SERVICE_WITH_GSON_SIGNIN) private val serviceForSignIn: ApiInterface,
    @Named(DEVICE_ID) private val deviceId: String,
    @Named(APP_NAME) private val appName: String,
    private val userDataDao: UserDataDao
) {
    suspend fun users(
        username: String, password: String
    ): Response<List<LoginResponse>> {
        Log.d("TAG", "users: "+"username"+username+"\n"+"pass"+password)
        return serviceForLogin.users(
            auth = AUTHORIZATION_KEY,
            username = username,
            password = password,
            appName = appName,
            deviceId = deviceId,
        )
    }
    suspend fun signin(username: String, password: String, loginRequest: LoginRequest):Response<SigninResponse>{
        return serviceForSignIn.signin(loginRequest)
    }
    suspend fun register(registerationRequest :RegisterationRequest):Response<RegisterResponse>{
        return serviceForSignIn.register(registerationRequest = registerationRequest)
    }
}