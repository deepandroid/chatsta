package com.tridhya.chatsta.design.fragments.settings.transactions

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentAmountDueBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.provider.Constants

class AmountDueFragment :
    BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAmountDueBinding

    //    private val viewModel by lazy { TransactionsViewModel(requireContext()) }
    private var user: User? = null
    private var type: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAmountDueBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        user = (context as ActivityBase).session?.user
        val arguments = arguments
        if (arguments != null) {
            type = arguments.getString("paymentType")
        }
//        viewModel.isLoading.value = true
//        viewModel.getDonationHistory()
//        viewModel.getProfile(session?.user?.userId.toString())
        setObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        if (type == Constants.INCOME) {
            setIncomeLayout()
        } else {
//            binding.tvDonationHistory.text =
//                "View donations history of this payment.\n(10.09.2021 - 11.09.2021)"
        }
        binding.tvDonationHistory.paintFlags =
            binding.tvDonationHistory.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.ivClose.setOnClickListener(this)
        binding.btnMakePayment.setOnClickListener(this)
        binding.tvDonationHistory.setOnClickListener(this)
        binding.tvAmountDue.text = user?.totalDue.toString()
        if (binding.tvAmountDue.text.toString()
                .isNotBlank() && binding.tvAmountDue.text.toString()
                .isNotEmpty() && binding.tvAmountDue.text.toString() != "0"
        ) {
            binding.btnMakePayment.isEnabled = true
            binding.btnMakePayment.backgroundTintList = context?.getColor(R.color.yellow)
                ?.let { ColorStateList.valueOf(it) }
        }
    }

    private fun setIncomeLayout() {
        binding.tvTitle.text = context?.getString(R.string.income)
        binding.tvSubTitle.text = context?.getString(R.string.income_earnings_info)
//        binding.tvDonationHistory.text =
//            "View withdraw history of these earnings.\n(10.09.2021 - 11.09.2021)"
        binding.tvPaymentTitle.text = context?.getString(R.string.earning)
        binding.tvPaymentSubTitle.text = context?.getString(R.string.income)
        binding.btnMakePayment.text = context?.getString(R.string.withdraw_earning)
    }

    private fun setObservers() {
        /*viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showProgressbar()
            else hideProgressbar()
        }
        viewModel.responseDonationHistory.observe(viewLifecycleOwner) {
            if (it.data?.donationHistoryList.isNullOrEmpty()) {
                binding.tvDonationHistory.gone()
            } else {
                binding.tvDonationHistory.visible()
                binding.tvDonationHistory.text =
                    "View donations history of this payment.\n(${it.data?.startDate} - ${it.data?.endDate})"
            }
            if (viewModel.responseUserData.value != null) {
                viewModel.isLoading.value = false
            }
        }
        viewModel.responseUserData.observe(viewLifecycleOwner) {
            session?.user = it
            binding.tvAmountDue.text = "$${session?.user?.totalDue}"
            if (viewModel.responseDonationHistory.value != null) {
                viewModel.isLoading.value = false
            }
        }*/
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigateUp()
            }

            R.id.btnMakePayment -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigate(R.id.to_stripe_payment)
            }

            R.id.tvDonationHistory -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigate(R.id.to_donation_history)
            }
        }
    }

}