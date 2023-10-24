package com.tridhya.basesetupnew.design.login

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridhya.basesetupnew.BR
import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.Service.ApiState
import com.tridhya.basesetupnew.Service.Status
import com.tridhya.basesetupnew.repository.LoginRepository
import com.tridhya.basesetupnew.utils.Constant.SHARED_COMMON
import com.tridhya.basesetupnew.utils.Constant.dismissProgress
import com.tridhya.basesetupnew.utils.Constant.showProgress
import com.tridhya.basesetupnew.utils.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: LoginRepository,
    private val propertyChangeRegistry: PropertyChangeRegistry,
    @Named(SHARED_COMMON) private val sharedPreferences: PrefUtils
):ViewModel(),Observable {

    val parentView:ObservableField<View> = ObservableField()
    private val _loginUserResponse: MutableLiveData<ApiState> = MutableLiveData()

    val loginUserResponse: LiveData<ApiState>
        get() {
            return _loginUserResponse
        }
    private val email: ObservableField<String> = ObservableField("")
    private val password: ObservableField<String> = ObservableField("")

    @Bindable
    fun getEmailXml(): String {
        return email.get().toString()
    }

    fun setEmailXml(mail: String) {
        email.set(mail)
        propertyChangeRegistry.notifyChange(this, BR.emailXml)
    }

    @Bindable
    fun getPasswordXml(): String {
        return password.get().toString()
    }

    fun setPasswordXml(pwd: String) {
        password.set(pwd)
        propertyChangeRegistry.notifyChange(this, BR.passwordXml)
    }
    fun loginUser() {
        if (getEmailXml().isEmpty()) {
            _loginUserResponse.postValue(
                ApiState(
                    Status.ERROR, parentView.get()!!.context.getString(
                        R.string.pleaseEnterUsername
                    )
                )
            )
            return
        }
        if (getPasswordXml().isEmpty()) {
            _loginUserResponse.postValue(
                ApiState(
                    Status.ERROR, parentView.get()!!.context.getString(
                        R.string.pleaseEnterPincode
                    )
                )
            )
            return
        }

        showProgress(parentView.get()!!.context)
        viewModelScope.launch {
            val response = repository.userLogin(
                parent = parentView.get(),
                username = getEmailXml(),
                password = getPasswordXml(),
                isSuccessMessageShow = false,
                isFailureMessageShow = true
            )
            launch {
                Log.d("TAG", "getApiStateResponseStatus: "+"inside login view model")
                dismissProgress(parentView.get()!!.context)
                _loginUserResponse.postValue(response)
            }
        }
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.remove(callback)
    }
}