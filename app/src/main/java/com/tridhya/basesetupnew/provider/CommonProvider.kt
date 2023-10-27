package com.tridhya.basesetupnew.provider

import android.content.Context

import androidx.databinding.PropertyChangeRegistry
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intuit.sdp.BuildConfig

import com.tridhya.basesetupnew.Service.ApiInterface
import com.tridhya.basesetupnew.Service.HeaderLoggingIntercepter
import com.tridhya.basesetupnew.Service.NetworkConstants
import com.tridhya.basesetupnew.utils.Constant

import com.tridhya.basesetupnew.utils.Constant.SHARED_COMMON
import com.tridhya.basesetupnew.utils.PrefUtils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonProvider {



    @Singleton
    @Named(SHARED_COMMON)
    @Provides
    fun providesSharedPreference(@ApplicationContext context: Context): PrefUtils =
        PrefUtils(context)

    @Singleton
    @Provides
    fun providesPropertyChangeRegistry(): PropertyChangeRegistry = PropertyChangeRegistry()






    @Singleton
    @Provides
    @Named(Constant.GSON)
    fun gson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    @Named(Constant.OK_HTTPS)
    fun providesOkHttpClient(
        @ApplicationContext context: Context,

        @Named(Constant.GSON) gson: Gson,

        @Named(SHARED_COMMON) preference: PrefUtils
    ): OkHttpClient {
        val okhttp = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS).addInterceptor(
                HttpLoggingInterceptor()
            ).addInterceptor(HeaderLoggingIntercepter())
        if (BuildConfig.DEBUG) okhttp.addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        )
        return okhttp.build()
    }

    @Provides
    @Named(Constant.SERVICE_FOR_LOGIN_GSON)
    @Singleton
    fun providesApiServiceForLoginGson(
        @Named(Constant.OK_HTTPS) okHttpClient: OkHttpClient,
        @Named(Constant.GSON) gson: Gson,
    ): ApiInterface {
        return Retrofit.Builder().baseUrl(NetworkConstants.ApiUrl.LOGIN_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Named(Constant.SERVICE_WITH_GSON_SIGNIN)
    @Singleton
    fun providesApiServiceForSignINGson(
        @Named(Constant.OK_HTTPS) okHttpClient: OkHttpClient,
        @Named(Constant.GSON) gson: Gson,
    ): ApiInterface {
        return Retrofit.Builder().baseUrl(NetworkConstants.ApiUrl.SIGNIN_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiInterface::class.java)
    }
}