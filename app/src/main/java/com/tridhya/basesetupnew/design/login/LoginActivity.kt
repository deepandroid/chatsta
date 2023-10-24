package com.tridhya.basesetupnew.design.login

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.base.ActivityBase
import com.tridhya.basesetupnew.Service.Status
import com.tridhya.basesetupnew.databinding.ActivityLoginBinding
import com.tridhya.basesetupnew.response.LoginResponse
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.IS_LOGIN
import com.tridhya.basesetupnew.utils.Constant.LOGIN_PINCODE
import com.tridhya.basesetupnew.utils.Constant.LOGIN_USER_NAME
import com.tridhya.basesetupnew.utils.Constant.USER_ID
import com.tridhya.basesetupnew.utils.Constant.dismissProgress
import com.tridhya.basesetupnew.utils.Constant.showProgress
import com.tridhya.basesetupnew.utils.Constant.smallToast
import kotlinx.coroutines.runBlocking


class LoginActivity : ActivityBase() {
    private val binding: ActivityLoginBinding by binding(R.layout.activity_login)
    private val viewModelLogin by viewModels<LoginViewModel>()
    private lateinit var loginResponse: List<LoginResponse>
    private val loginUserName: String? by lazy {
        intent.extras?.getString(Constant.INTENT_LOGIN_USER_NAME) ?: ""
    }
    private val loginPassword: String? by lazy {
        intent.extras?.getString(Constant.INTENT_LOGIN_PASSWORD) ?: ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@LoginActivity
            viewModel = viewModelLogin
            preference = sharedPref
            constant = Constant
            viewModelLogin.parentView.set(clParent)
           // tvLoginVersionCodeName.text = getString(R.string.v, versionCode, versionName)


            etEmail.setText(loginUserName)
            etPassword.setText(loginPassword)
            logout(true)
            clickListener()
            observer()
        }

    }
    private fun clickListener() {

    }

    private fun observer() {

        viewModelLogin.loginUserResponse.observe(this) {
            when (it.localStatus) {
                Status.SUCCESS -> {
                    val response = it.response as List<LoginResponse>
                    Log.d("TAG", "observer: "+response.toString())
                    if (response.isNotEmpty()) {
                        loginResponse = response
                     //   Log.d("TAG", "observer: "+response.toString())
                        showProgress(this@LoginActivity)
//                        firebaseSignIn(
//                            binding.etLoginEmail.text.toString(),
//                            binding.etLoginPassword.text.toString(),
//                            firebaseLoginSignUpStatusListener
                      //  afterFirebaseLogin()
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
    private fun afterFirebaseLogin() {
        sharedPref.putString(USER_ID, loginResponse[0].userId.toString())
        sharedPref.putBoolean(IS_LOGIN, true)
        sharedPref.putString(
            LOGIN_USER_NAME, binding.etEmail.text.toString()
        )
        sharedPref.putString(LOGIN_PINCODE, binding.etPassword.text.toString())

        runBlocking {
            dismissProgress(this@LoginActivity)
//            if (checkForSync()) viewModelLogin.goToDashBoard(
//                this@LoginActivity, loginResponse[0]
//            )
//            else viewModelLogin.goToSync(this@LoginActivity)
            Log.d("TAG", "afterFirebaseLogin: "+"Success")
        }
    }


}