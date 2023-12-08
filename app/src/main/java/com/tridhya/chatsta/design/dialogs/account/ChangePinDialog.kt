package com.tridhya.chatsta.design.dialogs.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutChangePinBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.dialogs.MessageDialog
import com.tridhya.chatsta.design.viewModel.ChangeCredentialsViewModel
import com.tridhya.chatsta.provider.Constants

class ChangePinDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: LayoutChangePinBinding
    private val viewModel by lazy { ChangeCredentialsViewModel(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutChangePinBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        /*        viewModel.responseChangePin.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    val savedLogin = (context as BaseActivity).session?.savedLogin
                    savedLogin?.pin = it.data?.pin
                    (context as ActivityBase).session?.savedLogin = savedLogin
                    MessageDialog.getInstance(
                        requireContext(),
                        it.message,
                        R.style.DefaultThemeDialog
                    )
                        .setIcon(R.drawable.ic_check_purple)
                        .setPositiveButtonText(R.string.ok)
                        .setListener(object : MessageDialog.ButtonListener {
                            override fun onPositiveBtnClicked(dialog: MessageDialog) {
                                dialog.dismiss()
                                dismiss()
                            }
                        })
                        .show()
                }*/
    }

    private fun initViews() {
        binding.etCurrentPin.editText.setOnFocusChangeListener { view, b ->
            binding.etCurrentPin.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
        binding.etNewPin.editText.setOnFocusChangeListener { view, b ->
            binding.etNewPin.tvTitle.isSelected =
                if (!b && (!(view as AppCompatEditText).text.isNullOrBlank())) {
                    view.isSelected = true
                    true
                } else {
                    view.isSelected = b
                    b
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        if (arguments?.containsKey(Constants.OLD_PIN) == true) {
            viewModel.oldPin.set(arguments?.getString(Constants.OLD_PIN))
            binding.etCurrentPin.tvTitle.isSelected = true
        }
        if (arguments?.containsKey(Constants.NEW_PIN) == true) {
            viewModel.newPin.set(arguments?.getString(Constants.NEW_PIN))
            binding.etCurrentPin.tvTitle.isSelected = true
        }

        binding.ivClose.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.etCurrentPin.editText.setOnClickListener {
            (context as ActivityBase).preventDoubleClick(it)
            val bundle = Bundle()
            bundle.putString(Constants.TAG, Constants.OLD_PIN)
            findNavController().navigate(R.id.to_create_pin, bundle)
        }
        binding.etNewPin.editText.setOnClickListener {
            (context as ActivityBase).preventDoubleClick(it)
            val bundle = Bundle()
            bundle.putString(Constants.TAG, Constants.NEW_PIN)
            findNavController().navigate(R.id.to_create_pin, bundle)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                (context as ActivityBase).preventDoubleClick(view)
                dismiss()
            }

            R.id.btnSave -> {
                (context as ActivityBase).preventDoubleClick(view)
                validate()
            }
        }
    }

    fun validate() {
        when {
            viewModel.oldPin.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_pin))
            }

            viewModel.newPin.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_pin))
            }

            else -> {
//                viewModel.isLoading.value = true
                MessageDialog.getInstance(
                    requireContext(),
                    "change pin",
                    R.style.DefaultThemeDialog
                )
                    .setIcon(R.drawable.ic_check_purple)
                    .setPositiveButtonText(R.string.ok)
                    .setListener(object : MessageDialog.ButtonListener {
                        override fun onPositiveBtnClicked(dialog: MessageDialog) {
                            dialog.dismiss()
                            dismiss()
                        }
                    })
                    .show()
//                viewModel.changePin()
            }
        }
    }
}