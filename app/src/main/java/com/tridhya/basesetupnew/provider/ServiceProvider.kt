package com.tridhya.basesetupnew.provider

import com.google.gson.Gson

import com.tridhya.basesetupnew.Service.ApiClient
import com.tridhya.basesetupnew.Service.ApiInterface
import com.tridhya.basesetupnew.Service.NetworkConstants.ApiUrl.HOME_URL
import com.tridhya.basesetupnew.dao.UserDataDao
import com.tridhya.basesetupnew.database.BrifecaseDataBase
import com.tridhya.basesetupnew.utils.Constant.API_CLIENT
import com.tridhya.basesetupnew.utils.Constant.APP_NAME
import com.tridhya.basesetupnew.utils.Constant.BASE_URL
import com.tridhya.basesetupnew.utils.Constant.DEVICE_ID
import com.tridhya.basesetupnew.utils.Constant.GSON
import com.tridhya.basesetupnew.utils.Constant.OK_HTTPS
import com.tridhya.basesetupnew.utils.Constant.SERVICE_FOR_LOGIN_GSON
import com.tridhya.basesetupnew.utils.Constant.SERVICE_WITH_GSON
import com.tridhya.basesetupnew.utils.Constant.SERVICE_WITH_GSON_SIGNIN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
object ServiceProvider {

    @ActivityRetainedScoped
    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl(brifecaseDataBase: BrifecaseDataBase): String =
        if (brifecaseDataBase.userDataDao().getUserData()
                .isNotEmpty()
        ) brifecaseDataBase.userDataDao().getUserData()[0].ip ?: HOME_URL else HOME_URL

    @ActivityRetainedScoped
    @Provides
    @Named(API_CLIENT)
    fun providesApiClient(
        @Named(SERVICE_WITH_GSON) service: ApiInterface,
        @Named(SERVICE_FOR_LOGIN_GSON) serviceForLogin: ApiInterface,
        @Named(SERVICE_WITH_GSON_SIGNIN) serviceForSignin: ApiInterface,

        @Named(DEVICE_ID) deviceId: String,
        @Named(APP_NAME) appName: String,
        userDataDao: UserDataDao,
    ): ApiClient = ApiClient(
        service = service,
        serviceForLogin = serviceForLogin,
        serviceForSignIn = serviceForSignin,
        deviceId = deviceId,
        appName = appName,
        userDataDao = userDataDao


    )

    @Provides
    @Named(SERVICE_WITH_GSON)
    @ActivityRetainedScoped
    fun providesApiService(
        @Named(OK_HTTPS) okHttpClient: OkHttpClient,
        @Named(BASE_URL) baseUrl: String,
        @Named(GSON) gson: Gson
    ): ApiInterface {

        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiInterface::class.java)
    }

}