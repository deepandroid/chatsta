package com.tridhya.basesetupnew.design.register

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.base.ActivityBase
import com.tridhya.basesetupnew.Service.Status
import com.tridhya.basesetupnew.databinding.ActivityRegisterBinding
import com.tridhya.basesetupnew.response.Register.RegisterResponse
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.smallToast

class RegisterActivity : ActivityBase() {
    private val binding: ActivityRegisterBinding by binding(R.layout.activity_register)
    private val viewModelRegister by viewModels<RegisterViewModel>()
    private lateinit var registerResponse: RegisterResponse
    private val registerUsername: String? by lazy {
        intent.extras?.getString(Constant.INTENT_LOGIN_USER_NAME) ?: ""
    }

    private val registerPassword: String? by lazy {
        intent.extras?.getString(Constant.INTENT_LOGIN_PASSWORD) ?: ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@RegisterActivity
            viewModel = viewModelRegister
            preference = sharedPref
            constant = Constant
            viewModelRegister.parentView.set(clParent)
            // tvLoginVersionCodeName.text = getString(R.string.v, versionCode, versionName)


            etEmail.setText(registerUsername)
            etEmail.setText(registerUsername)
            etPassword.setText(registerPassword)
            logout(true)
            clickListener()
            observer()
        }
    }
    private fun clickListener() {

    }
    private fun observer() {
        viewModelRegister.registerUserResponse.observe(this){
            Log.d("TAG", "SigninResponseData: "+it.response.toString())
            when (it.localStatus) {
                Status.SUCCESS -> {
                    val response = it.response as RegisterResponse

                    if (response != null) {
                        registerResponse = response

                        if (it.response.code==0){
                            smallToast(it.response.data.email)
                        }else{
                            smallToast(it.response.message)
                        }

//                        )
                        Log.d("TAG", "observer: "+"correct")
                    } else {
                        Log.d("TAG", "observer: "+"wrong")
                        smallToast(getString(R.string.wrongCredential))
                    }
                }

                Status.UNAUTHORISED -> {
                    logout()
                }

                Status.ERROR -> {
                    smallToast(it.localError.toString())
                }

                else -> {
                    smallToast(it.localError.toString())
                    binding.etPassword.requestFocus()
                }
            }
        }
    }
}