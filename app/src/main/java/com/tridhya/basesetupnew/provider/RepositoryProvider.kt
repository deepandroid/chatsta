package com.tridhya.basesetupnew.provider

import com.google.gson.Gson

import com.tridhya.basesetupnew.Service.ApiClient
import com.tridhya.basesetupnew.repository.Signin.SigninRepository
import com.tridhya.basesetupnew.utils.Constant.API_CLIENT
import com.tridhya.basesetupnew.utils.Constant.GSON
import com.tridhya.basesetupnew.utils.Constant.SHARED_COMMON
import com.tridhya.basesetupnew.utils.PrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryProvider {

    @Provides
    @ActivityRetainedScoped

    fun signinRepository(

        @Named(GSON) gson: Gson,
        @Named(API_CLIENT) client: ApiClient,
        @Named(SHARED_COMMON) prefUtils: PrefUtils,

    ): SigninRepository = SigninRepository(
         client, prefUtils
    )


}