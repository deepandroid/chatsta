package com.tridhya.basesetupnew.provider

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import androidx.databinding.PropertyChangeRegistry
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intuit.sdp.BuildConfig

import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.Service.ApiInterface
import com.tridhya.basesetupnew.Service.HeaderLoggingIntercepter
import com.tridhya.basesetupnew.Service.NetworkConstants
import com.tridhya.basesetupnew.Service.NetworkStatusInterceptor
import com.tridhya.basesetupnew.database.BrifecaseDataBase
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.APP_NAME
import com.tridhya.basesetupnew.utils.Constant.DEVICE_MODEL
import com.tridhya.basesetupnew.utils.Constant.SHARED_COMMON
import com.tridhya.basesetupnew.utils.Constant.VERSION_CODE
import com.tridhya.basesetupnew.utils.Constant.VERSION_NAME
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
    @Provides
    @Named(APP_NAME)
    fun provideAppName(
        @ApplicationContext context: Context
    ): String {
        return context.getString(R.string.app_name)
    }

    @Singleton
    @Provides
    @Named(DEVICE_MODEL)
    fun provideDeviceModel(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return "$manufacturer $model"
    }


    @Singleton
    @Named(SHARED_COMMON)
    @Provides
    fun providesSharedPreference(@ApplicationContext context: Context): PrefUtils =
        PrefUtils(context)

    @Singleton
    @Provides
    fun providesPropertyChangeRegistry(): PropertyChangeRegistry = PropertyChangeRegistry()


//    @Singleton
//    @Provides
//    fun providesFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
//        FirebaseAnalytics.getInstance(context)

    @Singleton
    @Provides
    @Named(VERSION_CODE)
    fun providesVersionCode(@ApplicationContext context: Context): String {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName, PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        return PackageInfoCompat.getLongVersionCode(packageInfo).toString()
    }

    @Singleton
    @Provides
    @Named(VERSION_NAME)
    fun providesVersionName(@ApplicationContext context: Context): String {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName, PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        return packageInfo.versionName
    }

    @Singleton
    @Provides
    @Named(Constant.GSON)
    fun gson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    @Named(Constant.OK_HTTPS)
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        brifecaseDataBase: BrifecaseDataBase,
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