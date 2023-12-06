package com.tridhya.chatsta.Service

import com.tridhya.chatsta.utils.Constant.SERVICE_WITH_GSON_SIGNIN
import javax.inject.Inject
import javax.inject.Named

class ApiClient @Inject constructor(
    @Named(SERVICE_WITH_GSON_SIGNIN) private val serviceForSignIn: ApiInterface, ) {

    /*    suspend fun signin(loginRequest: LoginRequest):Response<SigninResponse>{
            return serviceForSignIn.signin(loginRequest)
        }
        suspend fun register(registerationRequest : RegisterationRequest):Response<RegisterResponse>{
            return serviceForSignIn.register(registerationRequest = registerationRequest)
        }*/
}