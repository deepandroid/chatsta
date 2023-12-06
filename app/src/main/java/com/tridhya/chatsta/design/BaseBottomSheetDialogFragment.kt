package com.tridhya.chatsta.design

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.utils.ProgressDialog

open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var progressDialog: ProgressDialog? = null
    private var snackbar: Snackbar? = null
    private var permissionListener: ActivityBase.PermissionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isFitToContents = true
            skipCollapsed = true
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.add(this, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun dismissDialog() {
        dismissAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.dismiss()
    }

    fun showProgressbar() {
        hideProgressbar()
        if (progressDialog == null) {
            progressDialog = ProgressDialog(requireContext())
        }
        progressDialog?.show()
    }

    fun hideProgressbar() {
        progressDialog?.dismiss()
    }

    fun showToastShort(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showSnackbar(view: View, msg: String, LENGTH: Int) {
        snackbar = Snackbar.make(view, msg, LENGTH)
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackbar?.show()
    }

//    fun showSnackbar(
//        view: View,
//        msg: String,
//        LENGTH: Int,
//        action: String?,
//        actionListener: SnackbarActionListener?
//    ) {
//        snackbar = Snackbar.make(view, msg, LENGTH)
//        snackbar?.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.pale_purple))
//        if (actionListener != null) {
//            snackbar?.setAction(action) {
//                snackbar?.dismiss()
//                actionListener.onAction()
//            }
//        }
//        val sbView = snackbar?.view
//        val textView =
//            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
//        textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//        snackbar?.show()
//    }

    fun requestAppPermissions(
        requestedPermissions: Array<String?>,
        requestCode: Int, listener: ActivityBase.PermissionListener?,
    ) {
        permissionListener = listener
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permission in requestedPermissions) {
            permissionCheck += ContextCompat.checkSelfPermission(
                requireContext(),
                permission.toString()
            )
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), requestedPermissions, requestCode)
        } else {
            permissionListener?.onPermissionGranted(requestCode)
        }
    }

    fun showPermissionSettingDialog(message: String?) {
        val builder =
            AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.permission_required)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.app_settings) { dialog, which ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(intent)
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
        builder.create().show()
    }

}