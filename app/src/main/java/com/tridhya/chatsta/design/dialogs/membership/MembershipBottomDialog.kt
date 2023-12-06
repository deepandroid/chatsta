package com.tridhya.chatsta.design.dialogs.membership

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutMembershipBottomDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.dialogs.MemberShipDialog
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible

class MembershipBottomDialog : BaseBottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: LayoutMembershipBottomDialogBinding
//    private val viewModel by lazy { TransactionsViewModel(requireContext()) }

//    private val purchasesUpdatedListener =
//        PurchasesUpdatedListener { billingResult, purchases ->
//            // To be implemented in a later section.
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutMembershipBottomDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
//        viewModel.isLoading.value = true
//        viewModel.getProfile(Networking.session?.user?.userId.toString())
        setObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivClose.setOnClickListener(this)
        binding.tvChoosePlan.setOnClickListener(this)
        /* val billingClient = BillingClient.newBuilder(requireContext())
             .setListener(purchasesUpdatedListener)
             .enablePendingPurchases()
             .build()
         billingClient.startConnection(object : BillingClientStateListener {
             override fun onBillingSetupFinished(billingResult: BillingResult) {
                 if (billingResult.responseCode == OK) {
                     // The BillingClient is ready. You can query purchases here.
                 }
             }

             override fun onBillingServiceDisconnected() {
                 // Try to restart the connection on the next request to
                 // Google Play by calling the startConnection() method.
             }
         })*/
    }

    private fun setObservers() {
        /* viewModel.isLoading.observe(viewLifecycleOwner) {
             if (it) showProgressbar()
             else hideProgressbar()
         }
         viewModel.responseUserData.observe(viewLifecycleOwner) {
             viewModel.isLoading.value = false
             (context as BaseActivity).session?.user = it
             if (it.isMember == true) {
                 binding.tvChoosePlan.text = getString(R.string.premiun)
             }
         }*/
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dismiss()
            }

            R.id.tvChoosePlan -> {
                (context as ActivityBase).preventDoubleClick(view)
                if ((context as ActivityBase).session?.user?.isMember == false) {
                    binding.llChoosePlan.gone()
                    MemberShipDialog(requireContext()).setListener(object :
                        MemberShipDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MemberShipDialog) {
                            getPlan()
                            dialog.dismiss()
                        }

                        override fun onNegativeBtnClicked(dialog: MemberShipDialog) {
                            dialog.dismiss()
                            if ((context as ActivityBase).session?.user?.isMember == false) {
                                binding.llChoosePlan.visible()
                            }
                        }

                    }).show()
                }
            }
        }
    }

    private fun getPlan() {

    }

//    private fun setupBillingClient() {
//        val billingClient = BillingClient
//            .newBuilder(requireContext())
//            .setListener(this)
//            .build()
//
//        billingClient.startConnection(object : BillingClientStateListener {
//
//            override fun onBillingServiceDisconnected() {
//                println("BILLING | onBillingServiceDisconnected | DISCONNECTED")
//            }
//
//            override fun onBillingSetupFinished(billingResponseCode: BillingResult) {
//                if (billingResponseCode == BillingClient.BillingResponseCode.OK) {
//                    println("BILLING | startConnection | RESULT OK")
//                } else {
//                    println("BILLING | startConnection | RESULT: $billingResponseCode")
//                }
//            }
//        })
//    }

}