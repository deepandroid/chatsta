package com.tridhya.chatsta.design.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentPinEnterBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.provider.Constants


class EnterPinFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentPinEnterBinding
    private var tagTxt = "pin"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPinEnterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.containsKey(Constants.TAG) == true) {
            tagTxt = arguments?.getString(Constants.TAG).toString()
            if (tagTxt == Constants.NEW_PIN)
                binding.tvtitle.text = getString(R.string.title_new_pin)
            else if (tagTxt == Constants.OLD_PIN)
                binding.tvtitle.text = getString(R.string.title_current_pin)
            binding.btnCreate.text = getString(R.string.enter)
        }
        binding.tv0.setOnClickListener(this)
        binding.tv1.setOnClickListener(this)
        binding.tv2.setOnClickListener(this)
        binding.tv3.setOnClickListener(this)
        binding.tv4.setOnClickListener(this)
        binding.tv5.setOnClickListener(this)
        binding.tv6.setOnClickListener(this)
        binding.tv7.setOnClickListener(this)
        binding.tv8.setOnClickListener(this)
        binding.tv9.setOnClickListener(this)
        binding.tvDelete.setOnClickListener(this)
        binding.btnCreate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv0 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "0")
            }

            R.id.tv1 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "1")
            }

            R.id.tv2 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "2")
            }

            R.id.tv3 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "3")
            }

            R.id.tv4 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "4")
            }

            R.id.tv5 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "5")
            }

            R.id.tv6 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "6")
            }

            R.id.tv7 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "7")
            }

            R.id.tv8 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "8")
            }

            R.id.tv9 -> {
                preventDoubleClick(view)
                binding.firstPinView.setText(binding.firstPinView.text.toString() + "9")
            }

            R.id.tvDelete -> {
                preventDoubleClick(view)
                if (!binding.firstPinView.text.isNullOrBlank()) {
                    var txt = binding.firstPinView.text.toString()
                    txt = txt.subSequence(0, txt.length - 1).toString()
                    binding.firstPinView.setText(txt)
                }
            }

            R.id.btnCreate -> {
                preventDoubleClick(view)
                if (!binding.firstPinView.text.isNullOrBlank() && binding.firstPinView.text?.length == 4) {
                    val navController: NavController =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

                    navController.previousBackStackEntry!!
                        .savedStateHandle.remove<String>("pin")
                    navController.previousBackStackEntry!!
                        .savedStateHandle.remove<String>(Constants.OLD_PIN)
                    navController.previousBackStackEntry!!
                        .savedStateHandle.remove<String>(Constants.NEW_PIN)
                    navController.previousBackStackEntry!!
                        .savedStateHandle.set<String>(tagTxt, binding.firstPinView.text.toString())
                    navController.navigateUp()
                }
            }
        }
    }
}