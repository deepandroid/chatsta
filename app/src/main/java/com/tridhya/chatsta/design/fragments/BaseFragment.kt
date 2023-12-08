package com.tridhya.chatsta.design.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcelable
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.tridhya.chatsta.utils.FilePathUtil
import com.tridhya.chatsta.utils.FileUtils
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.utils.Session
import com.yalantis.ucrop.UCrop
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


open class BaseFragment : Fragment() {

    var session: Session? = null
    var state: Parcelable? = null
    var uploadView: View? = null
    private var uri: Uri? = null
    val photoAdded: MutableLiveData<Boolean> = MutableLiveData()
    private var mLastClickTime: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        session = Session(context)
    }

    fun openWebUrl(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
//        if (intent.resolveActivity(requireActivity().packageManager) != null) {
        startActivity(intent)
//        }
    }

    fun showSoftKeyboard(editText: EditText?, activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = requireActivity().currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun openPickerDialog(v: View) {
        val options = arrayOf("Take Photo", "Select from Gallery", "Select from file")

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
//        builder.setTitle(getString(R.string.upload_file))
        builder.setItems(options) { dialog, pos ->
            when (pos) {
                0 -> {
                    openCamera(v)
                }

                1 -> {
                    openGallery(v)
                }

                2 -> {
//                    pickPdf(v)
                }
            }
        }
        builder.show()
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val path = uri
                val name = uri?.let { FilePathUtil.getFileName(requireContext(), it) }
                uploadView?.tag = path
                if (uploadView is AppCompatTextView) (uploadView as AppCompatTextView).text = name
                uploadView = null
                uri = null
            }
        }

    fun openCamera(v: View) {
        uploadView = v
        val contentResolver = requireActivity().contentResolver
        val cv = ContentValues()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        cv.put(MediaStore.Images.Media.TITLE, timeStamp)
        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        grantWritePermission(requireContext(), intent, uri!!)
        cameraResult.launch(intent)
    }


    private fun grantWritePermission(context: Context, intent: Intent, uri: Uri) {
        val resInfoList =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    fun openGallery(v: View) {
        TedImagePicker.with(requireContext()).mediaType(MediaType.IMAGE).start { uri ->
            val path = uri
            val name = FilePathUtil.getFileName(requireContext(), uri)
//                if (v is AppCompatTextView)
//                    v.text = name
//                v.tag = uri

            uploadView = v

            val destinationUri = Uri.fromFile(
                File(
                    requireContext().cacheDir, "IMG_" + System.currentTimeMillis()
                )
            )

            if (FileUtils(requireContext()).getFileSize(uri)!! < 10000000) {
                    UCrop.of(uri, destinationUri!!).withAspectRatio(1f, 1f)
                        .withMaxResultSize(1080, 768).start(requireContext(), this)
            } else {
                showToastShort(getString(R.string.ett_image_size))
            }

        }
    }

    fun openCalender(textView: AppCompatTextView, minDate: Long? = null, maxDate: Long? = null) {
        val calc = Calendar.getInstance()
        calc.timeInMillis = System.currentTimeMillis()

        val dialog = DatePickerDialog(
            requireContext(), { view, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                textView.text =
                    "${String.format("%02d", dayOfMonth)}/${String.format("%02d", month + 1)}/$year"
                textView.tag = cal.timeInMillis

            }, calc.get(Calendar.YEAR), calc.get(Calendar.MONTH), calc.get(Calendar.DAY_OF_MONTH)
        )

        if (maxDate != null) {
            dialog.datePicker.maxDate = maxDate
        }
        if (minDate != null) {
            dialog.datePicker.maxDate = minDate
        }

        dialog.show()
    }

    fun showToastShort(msg: String) {
        (activity as ActivityBase).showToastShort(msg)
    }

    fun showToastLong(msg: String) {
        (activity as ActivityBase).showToastLong(msg)
    }

    fun showProgressbar() {
        (activity as ActivityBase).showProgressbar()
    }

    fun hideProgressbar() {
        (activity as ActivityBase).hideProgressbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == UCrop.REQUEST_CROP) {
                    if (resultCode == Activity.RESULT_OK) {
                        photoAdded.value = true
                        val uri = data?.let { UCrop.getOutput(it) }
                        uploadView?.tag = uri
                        if (uploadView is AppCompatImageView)
                            Glide.with(requireContext()).load(uploadView?.tag)
                                .into(uploadView as AppCompatImageView)
                        else if (uploadView is AppCompatTextView) {
                            (uploadView as AppCompatTextView).text =
                                uri?.let { FilePathUtil.getFileName(requireContext(), it) }
                        }
                    } else {
                        photoAdded.value = false
                        Log.e("Ucrop Error", data?.let { UCrop.getError(it)?.printStackTrace() }.toString())
                    }
                    uploadView = null
                }
    }

    fun preventDoubleClick(view: View) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    fun getMessageIdChatWithMe(userId: String): String {
        val myUserId = session?.user?.userId.toString()
        return if (myUserId < userId) {
            "${myUserId}_${userId}"
        } else {
            "${userId}_${myUserId}"
        }
    }

}