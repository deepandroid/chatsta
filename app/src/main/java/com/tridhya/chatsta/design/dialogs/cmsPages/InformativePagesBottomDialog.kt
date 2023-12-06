package com.tridhya.chatsta.design.dialogs.cmsPages

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutInformativePagesDialogBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.viewModel.SettingsViewModel

class InformativePagesBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutInformativePagesDialogBinding
    private val viewModel by lazy { SettingsViewModel(requireContext()) }
    private var aboutUsLink: String? = null
    private var termsAndConditionsLink: String? = null
    private var privacyPolicyLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutInformativePagesDialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivClose.setOnClickListener(this)
        binding.tvAboutUs.setOnClickListener(this)
        binding.tvContactUs.setOnClickListener(this)
        binding.tvTermsandConditions.setOnClickListener(this)
        binding.tvPrivacyPolicy.setOnClickListener(this)
        viewModel.isLoading.value = true
//        viewModel.informativePagesInfo(VERSION_INFO)
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showProgressbar()
            else hideProgressbar()
        }
        /*viewModel.responseCMSInfo.observe(viewLifecycleOwner) {
            viewModel.version.set(it.data?.version.toString())
            viewModel.lastUpdateDate.set(it.data?.lastUpdate?.let { it1 ->
                DateTimeUtils.getFormattedDate(
                    it1
                )
            })
            viewModel.email.set(it.data?.email)
            viewModel.web.set(it.data?.website)
            viewModel.generalInformation.set(it.data?.generalInformation)
            aboutUsLink = it.data?.aboutUsLink
            termsAndConditionsLink = it.data?.termsAndConditionsLink
            privacyPolicyLink = it.data?.privacyPolicyLink
            viewModel.isLoading.value = false
        }*/
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dialog?.dismiss()
            }

            R.id.tvAboutUs -> {
                (context as ActivityBase).preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.about_us))
                bundle.putString("type", aboutUsLink)
                findNavController().navigate(R.id.cmsPagesDialog, bundle)
            }

            R.id.tvContactUs -> {
                (context as ActivityBase).preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.contact_us))
                bundle.putString("email", viewModel.email.get())
                bundle.putString("web", viewModel.web.get())
                bundle.putString("generalInformation", viewModel.generalInformation.get())
                findNavController().navigate(R.id.cmsPagesDialog, bundle)
            }

            R.id.tvTermsandConditions -> {
                (context as ActivityBase).preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.terms_and_conditions))
                bundle.putString("type", termsAndConditionsLink)
                findNavController().navigate(R.id.cmsPagesDialog, bundle)
            }

            R.id.tvPrivacyPolicy -> {

                (context as ActivityBase).preventDoubleClick(view)
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.privacy_policy))
                bundle.putString("type", privacyPolicyLink)
                findNavController().navigate(R.id.cmsPagesDialog, bundle)
            }
        }
    }
}