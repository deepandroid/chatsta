package com.tridhya.basesetupnew.base

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.design.signin.SigninActivity
import com.tridhya.basesetupnew.design.splash.SplashActivity
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.INTENT_LOGIN_PASSWORD
import com.tridhya.basesetupnew.utils.Constant.INTENT_LOGIN_USER_NAME
import com.tridhya.basesetupnew.utils.Constant.SHARED_COMMON
import com.tridhya.basesetupnew.utils.Constant.logE
import com.tridhya.basesetupnew.utils.Constant.logI
import com.tridhya.basesetupnew.utils.Constant.smallToastWithContext
import com.tridhya.basesetupnew.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint

import java.io.Serializable
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
@Suppress("UNCHECKED_CAST")
open class ActivityBase : DataBindingActivity() {
    private val tagName: String = javaClass.simpleName


    @Inject
    @Named(SHARED_COMMON)
    lateinit var sharedPref: PrefUtils


    private fun initBaseComponants() {


    }


    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBaseComponants()

    }

    fun logout(
        isDataClearOnly: Boolean? = false, userName: String? = null, password: String? = null
    ) {
        sharedPref.logout()

        logE(tagName, "logout: normal logout")
        if (isDataClearOnly == null || isDataClearOnly == false) {
            val intent = Intent(this, SigninActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(Constant.LOGOUT_SCREEN, Constant.LOGOUT)

            if (userName != null && password != null) {
                intent.putExtra(INTENT_LOGIN_USER_NAME, userName)
                intent.putExtra(INTENT_LOGIN_PASSWORD, password)
            }
            startActivity(intent)
            finishAffinity()
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    open fun onBackPress() {
        finish()
    }


}
