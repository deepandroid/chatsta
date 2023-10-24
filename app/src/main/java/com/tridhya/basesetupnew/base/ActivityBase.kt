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
import com.tridhya.basesetupnew.design.login.LoginActivity
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

    var currentLat = "0.0"
    var currentLong = "0.0"
    var currentLatAndLong = "0.0"
    var isLocationDataIsAccurate = false

    @Inject
    @Named(SHARED_COMMON)
    lateinit var sharedPref: PrefUtils


    private fun initBaseComponants() {

        isLocationDataIsAccurate = false

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
//        if (firebaseAuth.currentUser != null) firebaseAuth.signOut()
//        deleteAllTable()
        logE(tagName, "logout: normal logout")
        if (isDataClearOnly == null || isDataClearOnly == false) {
            val intent = Intent(this, LoginActivity::class.java)
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

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location").setMessage(
            """
            Your Locations Settings is set to 'Off'.
            Please Enable Location to use this app
            """.trimIndent()
        ).setCancelable(false).setPositiveButton(
            "Location Settings"
        ) { _: DialogInterface?, _: Int ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }
        dialog.show()
    }

    private fun showAlertForManifestLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Enable Location and Media Storage")
                .setMessage("Your Locations or Media Permission is not given.")
                .setCancelable(false).setPositiveButton(
                    "App Settings"
                ) { _: DialogInterface?, _: Int ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            dialog.show()
        }
    }


    open fun onBackPress() {
        finish()
    }

    fun <T : Serializable?> Intent.getSerializableExtr(keyName: String?, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializableExtra(
            keyName, clazz
        )
        else getSerializableExtra(keyName) as T?
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.PERMISSION_INTENT_REQ_CODE -> for (i in grantResults.indices) if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                logI("permission", "granted")
            } else {
                logI("permission", "not granted")
                showAlertForManifestLocation()
                break
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                logI("Service status", "Running")
                return true
            }
        }
        logI("Service status", "Not running")
        return false
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {

                    } else {
                        smallToastWithContext(
                            this, getString(R.string.permission_denied)
                        )
                    }
                }
            }
        }

    private fun requestAllFilesAccessPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else true
        return true
    }

    private fun showAlertForAllMediaAccess() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.all_media_access)).setMessage(
            getString(R.string.you_have_to_grant_us_your_device_media_access)
        ).setPositiveButton(
            getString(R.string.app_media_settings)
        ) { _: DialogInterface?, _: Int ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                requestPermissionLauncher.launch(intent)
            } else {
                smallToastWithContext(
                    this, getString(R.string.your_device_version_not_need_for_this_permission)
                )
            }
        }
        dialog.setCancelable(false)
        dialog.show()
    }


}
