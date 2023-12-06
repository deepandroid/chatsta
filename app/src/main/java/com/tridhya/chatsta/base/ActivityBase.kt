package com.tridhya.chatsta.base

import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.tridhya.chatsta.utils.Constant.SHARED_COMMON
import com.tridhya.chatsta.utils.PrefUtils
import com.tridhya.chatsta.utils.ProgressDialog
import com.tridhya.chatsta.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
open class ActivityBase : DataBindingActivity() {
    private var snackbar: Snackbar? = null
    private val tagName: String = javaClass.simpleName
    private var mLastClickTime: Long = 0
    var session: Session? = null
    private var progressDialog: ProgressDialog? = null
    var permissionListener: PermissionListener? = null

    @Inject
    @Named(SHARED_COMMON)
    lateinit var sharedPref: PrefUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBaseComponants()

    }

    fun showFullScreen() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        /*windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE*/
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        WindowCompat.setDecorFitsSystemWindows(window, false)
        windowInsetsController.isAppearanceLightNavigationBars = true
    }

    fun disableScreenShots() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun initBaseComponants() {
        session = Session(this)
    }


    override fun onResume() {
        super.onResume()

    }

    fun showToastShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showProgressbar() {
        hideProgressbar()
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        progressDialog?.show()
    }


    fun hideProgressbar() {
        progressDialog?.dismiss()
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

    interface PermissionListener {
        fun onPermissionGranted(requestCode: Int)
        fun onPermissionDenied(requestCode: Int)
        fun onPermissionNeverAsk(requestCode: Int)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionListener?.onPermissionGranted(requestCode)
                break
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                permissionListener?.onPermissionDenied(requestCode)
                break
            } else {
                permissionListener?.onPermissionNeverAsk(
                    requestCode
                )
                break
            }
        }
    }

    fun preventDoubleClick(view: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

}
