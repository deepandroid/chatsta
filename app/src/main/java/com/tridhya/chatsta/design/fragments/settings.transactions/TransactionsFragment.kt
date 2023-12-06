package com.tridhya.chatsta.design.fragments.settings.transactions

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentTransactionsBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.provider.Constants


class TransactionsFragment :
    BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentTransactionsBinding
//    private val viewModel by lazy { TransactionsViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        binding.lifecycleOwner = viewLifecycleOwner
//        viewModel.isLoading.value = true
//        viewModel.getProfile(session?.user?.userId.toString())
        setObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivClose.setOnClickListener(this)
        binding.cvIncome.setOnClickListener(this)
        binding.cvAmountDue.setOnClickListener(this)
    }

    private fun onBackPressed() {
        findNavController().previousBackStackEntry!!
            .savedStateHandle.set<Boolean>(Constants.TAG, true)
        findNavController().navigateUp()
    }

    private fun setObservers() {
        /*viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showProgressbar()
            else hideProgressbar()
        }
        viewModel.responseUserData.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            session?.user = it
            binding.cvIncome.isVisible =
                session?.user?.isPaidContentProvider == true || session?.user?.totalIncome!! > 0
            binding.tvDueAmount.text = "$${session?.user?.totalDue}"
            binding.tvIncomeAmount.text = "$${session?.user?.totalIncome}"
        }*/
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                onBackPressed()
            }

            R.id.cvAmountDue -> {
                (context as ActivityBase).preventDoubleClick(view)
//                if (binding.tvDueAmount.text.toString()
//                        .isNotBlank() && binding.tvDueAmount.text.toString()
//                        .isNotEmpty() && binding.tvDueAmount.text.toString() != "$0"
//                ) {
                val bundle = Bundle()
                bundle.putString("paymentType", Constants.AMOUNT_DUE)
                findNavController().navigate(R.id.to_amount_due, bundle)
//                }
            }

            R.id.cvIncome -> {
                (context as ActivityBase).preventDoubleClick(view)
//                if (binding.tvIncomeAmount.text.toString()
//                        .isNotBlank() && binding.tvIncomeAmount.text.toString()
//                        .isNotEmpty() && binding.tvIncomeAmount.text.toString() != "$0"
//                ) {
//                    val bundle = Bundle()
//                    bundle.putString("paymentType", Constants.INCOME)
//                    findNavController().navigate(R.id.amountDueDialog, bundle)
//                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        viewModel.isLoading.value = true
//        viewModel.getProfile(session?.user?.userId.toString())
    }
}