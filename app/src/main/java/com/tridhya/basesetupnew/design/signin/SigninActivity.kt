package com.tridhya.basesetupnew.design.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.tridhya.basesetupnew.R
import com.tridhya.basesetupnew.base.ActivityBase
import com.tridhya.basesetupnew.Service.Status
import com.tridhya.basesetupnew.databinding.ActivitySigninBinding
import com.tridhya.basesetupnew.design.register.RegisterActivity
import com.tridhya.basesetupnew.response.SignIn.SigninResponse
import com.tridhya.basesetupnew.utils.Constant
import com.tridhya.basesetupnew.utils.Constant.smallToast

class SigninActivity : ActivityBase(), View.OnClickListener {
    private val binding: ActivitySigninBinding by binding(R.layout.activity_signin)
    private val viewModelSignIn by viewModels<SigninViewModel>()
    private lateinit var signinResponse: SigninResponse
    private val loginUserName: String? by lazy {
        intent.extras?.getString(Constant.INTENT_LOGIN_USER_NAME) ?: ""
    }
    private val loginPassword: String? by lazy {
        intent.extras?.getString(Constant.INTENT_LOGIN_PASSWORD) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@SigninActivity
            viewModel = viewModelSignIn
            preference = sharedPref
            constant = Constant
            viewModelSignIn.parentView.set(clParent)


            etEmail.setText(loginUserName)
            etPassword.setText(loginPassword)
            logout(true)
            clickListener()
            observer()
        }
    }

    private fun clickListener() {
        Log.d("TAG", "clickListener: " + "inside")
        with(binding) {
            btnRegister.setOnClickListener(this@SigninActivity)


        }

    }

    private fun observer() {
        viewModelSignIn.signinUserResponse.observe(this) {
            Log.d("TAG", "SigninResponseData: " + it.localStatus.toString())
            when (it.localStatus) {
                Status.SUCCESS -> {
                    val response = it.response as SigninResponse

                    if (response != null) {
                        signinResponse = response

                        if (it.response.code==0){
                            smallToast(it.response.data.email)
                        }else{
                            smallToast(it.response.message)
                        }

//                        )
                        Log.d("TAG", "observer: " + "correct")
                    } else {
                        Log.d("TAG", "observer: " + "wrong")
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

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnRegister->{
                val i = Intent(this@SigninActivity, RegisterActivity::class.java)
                startActivity(i)

            }
        }
    }
}