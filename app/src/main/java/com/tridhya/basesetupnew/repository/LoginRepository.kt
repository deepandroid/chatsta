package com.tridhya.basesetupnew.repository

import android.view.View
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.tridhya.basesetupnew.Service.ApiClient
import com.tridhya.basesetupnew.Service.ApiState
import com.tridhya.basesetupnew.Service.NetworkConstants
import com.tridhya.basesetupnew.Service.NetworkConstants.getApiStateResponseStatus
import com.tridhya.basesetupnew.Service.ResponseState
import com.tridhya.basesetupnew.dao.UserDataDao
import com.tridhya.basesetupnew.response.LoginResponse
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.SHARED_COMMON
import com.tridhya.basesetupnew.utils.Constant.isNetWork
import com.tridhya.basesetupnew.utils.PrefUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class LoginRepository @Inject constructor(
    @Named(Constant.GSON) private val gson: Gson,
    @Named(Constant.API_CLIENT) private val apiClient: ApiClient,
    private val userDataDao: UserDataDao,
    @Named(SHARED_COMMON) private val prefUtils: PrefUtils,
){
    suspend fun userLogin(
        parent: View?,
        username: String,
        password: String,
        isSuccessMessageShow: Boolean,
        isFailureMessageShow: Boolean
    ): ApiState {
        val responseData: ResponseState?

        if (isNetWork(parent!!.context)) {
            val response = apiClient.users(
                username = username,
                password = password
            )
            val responseBody = response.body()
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
        } else
            responseData =
                ResponseState(
                    parentView = parent,
                    isNetworkAvailable = false
                )

        return getApiStateResponseStatus(
            responseData, prefUtils, funListener
        )
    }
    @WorkerThread
    suspend fun insertUserData(data: List<LoginResponse>) =
        withContext(Dispatchers.IO) {
            userDataDao.insertUserData(data)
        }
    private val funListener = fun(whichState: Int, data: Any?): Any? =
        runBlocking {

            when (whichState) {
                Constant.DELETE_DATA_IN_DATABASE -> {
                    null
                }
                Constant.INSERT_DATA_IN_DATABASE -> {
                    if (data != null)
                        insertUserData(data as List<LoginResponse>)
                    null
                }
                Constant.GET_DATA_IN_DATABASE -> {
                    null
                }
                else -> {
                    null
                }
            }
        }
}