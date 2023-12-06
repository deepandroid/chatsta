package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.provider.Constants


open class BaseViewModel(private val mContext: Context) : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    val errorLiveData: MutableLiveData<BaseErrorModel> = MutableLiveData()
    val refreshLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorDialog = MessageDialog.getInstance(mContext, R.style.ErrorThemeDialog)
        .setPositiveButtonText(R.string.ok)
        .setIcon(R.drawable.ic_warning)
        .setListener(object : MessageDialog.ButtonListener {
            override fun onPositiveBtnClicked(dialog: MessageDialog) {
                dialog.dismiss()
            }
        })

    init {
        errorLiveData.observeForever {
            if (it.code == 1) {
                refreshLiveData.value = true
            }
            if (it.message != Constants.ILLEGAL_STATE_EXCEPTION) {
                it.message?.let { it1 -> errorDialog.setMessage(it1).show() }
//                errorDialog.setMessage("Something went wrong. Please try again later.").show()
            }
            isLoading.value = false
            isRefreshing.value = false
        }

        isLoading.observe(mContext as ActivityBase) {
            if (it) mContext.showProgressbar()
            else mContext.hideProgressbar()
        }
    }

    override fun onCleared() {
        errorDialog.dismiss()
        super.onCleared()
    }

}