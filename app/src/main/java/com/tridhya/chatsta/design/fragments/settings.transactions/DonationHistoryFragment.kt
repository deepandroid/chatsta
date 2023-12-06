package com.tridhya.chatsta.design.fragments.settings.transactions

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.FragmentDonationHistoryBinding
import com.tridhya.chatsta.design.fragments.BaseFragment


class DonationHistoryFragment :
    BaseFragment(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentDonationHistoryBinding

    //    private val viewModel by lazy { TransactionsViewModel(requireContext()) }
    private var page: Int = 1
    /*private val donationHistoryAdapter by lazy {
        DonationHistoryAdapter(
            listener = this,
            loadMoreListener = this
        )
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentDonationHistoryBinding.inflate(
                layoutInflater,
                container,
                false
            )
            binding.lifecycleOwner = viewLifecycleOwner
            val user = (context as ActivityBase).session?.user
            initViews()
            page = 1
            /*viewModel.isLoading.value = true
            viewModel.getDonationHistory(page)*/
            setObservers()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivClose.setOnClickListener(this)
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    private fun initViews() {
        binding.rvDonationHistory.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvDonationHistory.adapter = donationHistoryAdapter
    }

    private fun setObservers() {
        /*        viewModel.isLoading.observe(viewLifecycleOwner) {
                    if (it) showProgressbar()
                    else hideProgressbar()
                }
                viewModel.isRefreshing.observe(viewLifecycleOwner) {
                    binding.swipeRefresh.isRefreshing = it
                }
                viewModel.responseDonationHistory.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    viewModel.isRefreshing.value = false
                    if (it.data?.donationHistoryList.isNullOrEmpty() && page == 1) {
                        binding.tvNoNotification.visible()
                        binding.rvDonationHistory.gone()
                    } else {
                        binding.tvSummary.text =
                            "Donations summary for ${it.data?.startDate} - ${it.data?.endDate}"
                        binding.rvDonationHistory.visible()
                        binding.tvNoNotification.gone()
                        if (page == 1) {
                            binding.rvDonationHistory.adapter = donationHistoryAdapter
                            donationHistoryAdapter.setList(it.data?.donationHistoryList)
                        } else {
                            donationHistoryAdapter.addList(it.data?.donationHistoryList)
                        }
                    }
                }*/
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                findNavController().navigateUp()
            }
        }
    }

    override fun onRefresh() {
        page = 1
//        viewModel.getDonationHistory(page)
    }
}