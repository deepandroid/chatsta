package com.tridhya.chatsta.design.fragments.settings.transactions

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.CardBrand
import com.stripe.android.model.Token
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentStripePaymentBinding
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.fragments.BaseFragment


class StripePaymentFragment :
    BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentStripePaymentBinding

    //    private val viewModel by lazy { TransactionsViewModel(requireContext()) }
    val data = MutableLiveData<Token>()
    private lateinit var paymentLauncher: PaymentLauncher
    private var paymentIntentId: String? = null
    private var paymentResult: String? = null
    private var paymentFailMessage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStripePaymentBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        setObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        val paymentConfiguration = PaymentConfiguration.getInstance(requireContext())
        binding.evCvv.hint = getString(R.string.cvv)
        paymentLauncher = PaymentLauncher.Companion.create(
            this@StripePaymentFragment,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId
        ) {
//            viewModel.isLoading.value = true
//            viewModel.getPayment(paymentIntentId.toString())
            /*            when (it) {
                            is PaymentResult.Completed -> {
                                Log.e("Stripe:---", "Payment Status -> $PAYMENT_COMPLETED")
                                paymentResult = PAYMENT_COMPLETED

                            }
                            is PaymentResult.Canceled -> {
                                Log.e("Stripe:---", "Payment Status -> $PAYMENT_CANCELED")
                                paymentResult = PAYMENT_CANCELED
                            }
                            is PaymentResult.Failed -> {
                                Log.e("Stripe:---", "Payment Status -> $PAYMENT_FAILED")
                                paymentResult = PAYMENT_FAILED
                                paymentFailMessage = it.throwable.message.toString()
                            }
                        }*/
        }
        binding.ivClose.setOnClickListener(this)
        binding.btnFinish.setOnClickListener(this)
        binding.evCardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val data =
                    CardBrand.fromCardNumber(binding.evCardNumber.text.toString().replace(" ", ""))
                Log.e("Stripe:---", "Card brand icon -> ${data.icon}")
                Log.e("Stripe:---", "Card brand name -> ${data.displayName}")
                if (data.code != CardBrand.Unknown.code) {
                    binding.evCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        data.icon,
                        0
                    )
                } else {
                    binding.evCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.stripe_ic_card_placeholder,
                        0
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setObservers() {
        /*
                viewModel.responsePaymentIntentId.observe(viewLifecycleOwner) {
        //            viewModel.isLoading.value = false
                    paymentIntentId = it.data?.paymentIntentId.toString()
                    val cardInputWidget = CardInputWidget(requireContext())
                    cardInputWidget.setCardNumber(binding.evCardNumber.text.toString())
                    cardInputWidget.setCvcCode(binding.evCvv.text.toString())
                    cardInputWidget.setExpiryDate(
                        binding.evExpiryDate.validatedDate?.month!!,
                        binding.evExpiryDate.validatedDate?.year!!
                    )
                    cardInputWidget.postalCodeRequired = false

                    val params = cardInputWidget.paymentMethodCreateParams
                    val confirmParams = params?.let { it1 ->
                        ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(
                                it1,
                                it.data?.paymentIntentClientSecretId.toString()
                            )
                    }
                    if (confirmParams != null) {
                        paymentLauncher.confirm(confirmParams)
                    }
                }

                viewModel.responseGetPayment.observe(viewLifecycleOwner) {
                    when (paymentResult) {
                        PAYMENT_COMPLETED -> {
                            showCustomDialog(
                                getString(R.string.payment_success),
                                R.style.DefaultThemeDialog,
                                R.drawable.ic_check_purple,
                                true
                            )
                        }
                        PAYMENT_CANCELED -> {
                            showCustomDialog(
                                getString(R.string.payment_failed),
                                R.style.DefaultDeleteThemeDialog,
                                R.drawable.ic_warning,
                                false
                            )
                        }
                        PAYMENT_FAILED -> {
                            showCustomDialog(
                                paymentFailMessage.toString(),
                                R.style.DefaultDeleteThemeDialog,
                                R.drawable.ic_warning,
                                false
                            )
                        }
                    }
                    viewModel.isLoading.value = false
                }
        */
    }

    private fun showCustomDialog(
        message: String,
        theme: Int,
        icon: Int,
        isDismissDialog: Boolean,
    ) {
        MessageDialog.getInstance(
            requireContext(),
            message,
            theme
        )
            .setIcon(icon)
            .setPositiveButtonText(R.string.ok)
            .setListener(object : MessageDialog.ButtonListener {
                override fun onPositiveBtnClicked(dialog: MessageDialog) {
                    dialog.dismiss()
                    if (isDismissDialog) {
                        findNavController().navigateUp()
                    }
                }
            })
            .show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigateUp()
            }

            R.id.btnFinish -> {
                (context as ActivityBase).preventDoubleClick(view)
//                (context as ActivityBase).hideKeyboard()
                validateDetails()
            }
        }
    }

    private fun validateDetails() {
        if (!binding.evCardNumber.isCardNumberValid) {
            showToastShort(getString(R.string.err_card_number))
        } else if (!binding.evExpiryDate.isDateValid) {
            showToastShort(getString(R.string.err_expiry_date))
        } else if (binding.evCvv.text.isNullOrBlank() && binding.evCvv.text.isNullOrEmpty() && binding.evCvv.text.toString().length <= 2) {
            showToastShort(getString(R.string.err_cvv))
        } else if (binding.evNameOnCard.text.isNullOrBlank() && !binding.evNameOnCard.text.isNullOrEmpty()) {
            showToastShort(getString(R.string.err_card_name))
        } else {
//            generateStripeToken()
//            viewModel.isLoading.value = true
//            viewModel.getPaymentIntent()
        }
    }
}