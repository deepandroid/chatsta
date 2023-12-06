package com.tridhya.chatsta.design.dialogs.account

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutAccountDialogBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.design.viewModel.ProfileViewModel
import com.tridhya.chatsta.provider.Constants
import com.tridhya.chatsta.provider.Constants.TAG

class AccountDialog :
    BaseFragment(), View.OnClickListener {

    private lateinit var binding: LayoutAccountDialogBinding
    private val viewModel by lazy { ProfileViewModel(requireContext()) }
//    private var mDBReference: DatabaseReference? = null

    private var currentPin = ""
    private var newPin = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutAccountDialogBinding.inflate(
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
        viewModel.isLoading.value = true
//        viewModel.getUserProfile(session?.user?.userId)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
//        Places.initialize(requireContext(), Constants.GOOGLE_MAP_KEYS)
        setObservers()
        return binding.root
    }

    private fun onBackPressed() {
        findNavController().previousBackStackEntry!!
            .savedStateHandle.set<Boolean>(TAG, true)
        findNavController().navigateUp()
    }

    private fun setObservers() {
        viewModel.responseUserData.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            (context as ActivityBase).session?.user = it
            viewModel.isPrivateAccount.set(session?.user?.isAccount)
            viewModel.isContentProvider.set(session?.user?.isPaidContentProvider)
            initViews()
            setUserData(it)
        }
    }

    private fun initViews() {
        binding.etLocation.editText.isClickable = true
        binding.etLocation.editText.isFocusable = false
        binding.etLocation.editText.isCursorVisible = false
        if (session?.user?.isPaidContentProvider == true) {
            binding.tvRequestToBePaidContentProvider.visibility = View.GONE
            binding.llStripeKey.visibility = View.VISIBLE
            binding.etEmail.editText.isEnabled = false
        } else {
            binding.tvRequestToBePaidContentProvider.visibility = View.VISIBLE
            binding.llStripeKey.visibility = View.GONE
            binding.etEmail.editText.isEnabled = true
        }
        binding.etFirstName.editText.onFocusChangeListener =
            View.OnFocusChangeListener { p0, selected ->
                binding.etFirstName.llInputText.isSelected = selected
                binding.etFirstName.tvTitle.isSelected = selected
            }

        binding.etFirstName.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etFirstName.editText.isSelected = true
                binding.etFirstName.tvTitle.isSelected = true
            }
        }
        binding.etLastName.editText.onFocusChangeListener =
            View.OnFocusChangeListener { p0, selected ->
                binding.etLastName.llInputText.isSelected = selected
                binding.etLastName.tvTitle.isSelected = selected
            }
        binding.etLastName.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etLastName.editText.isSelected = true
                binding.etLastName.tvTitle.isSelected = true
            }
        }
        binding.etUserName.editText.onFocusChangeListener =
            View.OnFocusChangeListener { p0, selected ->
                binding.etUserName.llInputText.isSelected = selected
                binding.etUserName.tvTitle.isSelected = selected
            }

        binding.etUserName.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etUserName.editText.isSelected = true
                binding.etUserName.tvTitle.isSelected = true
            }
        }
        binding.etEmail.editText.onFocusChangeListener =
            View.OnFocusChangeListener { p0, selected ->
                binding.etEmail.llInputText.isSelected = selected
                binding.etEmail.tvTitle.isSelected = selected
            }

        binding.etEmail.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etEmail.editText.isSelected = true
                binding.etEmail.tvTitle.isSelected = true
            }
        }
        binding.etLocation.editText.onFocusChangeListener =
            View.OnFocusChangeListener { p0, selected ->
                binding.etLocation.llInputText.isSelected = selected
                binding.etLocation.tvTitle.isSelected = selected
            }

        binding.etLocation.editText.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                binding.etLocation.editText.isSelected = true
                binding.etLocation.tvTitle.isSelected = true
            }
        }
        binding.etPassword.llInputText.isSelected = true
        binding.etPassword.tvTitle.isSelected = true
        binding.etPin.llInputText.isSelected = true
        binding.etPin.tvTitle.isSelected = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//        mDBReference = AppClass.mDBReference
        binding.ivClose.setOnClickListener(this)
        binding.btnOk.setOnClickListener(this)
        binding.tvRequestToBePaidContentProvider.setOnClickListener(this)
        binding.tvPermanentlyDeleteAccount.setOnClickListener(this)
        binding.etPassword.editText.inputType = InputType.TYPE_NULL
        binding.etPassword.editText.isFocusable = false
        binding.etPassword.editText.isFocusableInTouchMode = false
        binding.etPassword.editText.setOnClickListener {
            preventDoubleClick(it)
            findNavController().navigate(R.id.to_change_password)
        }
        binding.etPin.editText.inputType = InputType.TYPE_NULL
        binding.etPin.editText.isFocusable = false
        binding.etPin.editText.isFocusableInTouchMode = false
        binding.etPin.editText.setOnClickListener {
            preventDoubleClick(it)
            findNavController().navigate(R.id.to_change_pin)
        }

        binding.swPrivateAccount.setOnClickListener {
//            viewModel.isLoading.value = true
//            viewModel.privateAccount(session?.user?.userId)
        }

        binding.swPaidContentProvider.setOnClickListener {
//            viewModel.isLoading.value = true
//            viewModel.paidContentProviderToNormalAccount()
        }

        binding.etUserName.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val result: String = s.toString().replace(" ", "_")
                if (!s.toString().equals(result)) {
                    binding.etUserName.editText.setText(result)
                    binding.etUserName.editText.setSelection(result.length)
                }
            }

        })

        binding.etLocation.editText.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
//            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)

            // Start the autocomplete intent.
           /* val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext())
            startActivityForResult(intent, PLACE_PICKER_REQUEST)*/
        }
    }

    private fun setUserData(user: User) {
        viewModel.firstName.set(user.firstName)
        viewModel.lastName.set(user.lastName)
        viewModel.userName.set(user.username)
        viewModel.email.set(user.email)
        viewModel.location.set(user.location)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                preventDoubleClick(view)
                onBackPressed()
            }

            R.id.btnOk -> {
                preventDoubleClick(view)
                validate()
            }

            R.id.tvPermanentlyDeleteAccount -> {
                preventDoubleClick(view)
                showDeleteDialog()
            }

            R.id.tvRequestToBePaidContentProvider -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_request_for_content_provider)
            }
        }
    }

    private fun showDeleteDialog() {
        val deleteDialog = Dialog(requireContext(), R.style.DefaultDeleteThemeDialog)
        deleteDialog.setContentView(R.layout.dialog_delete_account)
        deleteDialog.show()
        val title = deleteDialog.findViewById<TextView>(R.id.tvTitle)
        val msg = deleteDialog.findViewById<TextView>(R.id.tvMessage)
        val btnCancel = deleteDialog.findViewById<TextView>(R.id.btnCancel)
        val btnOk = deleteDialog.findViewById<TextView>(R.id.btnOk)
        val ivIcon = deleteDialog.findViewById<ImageView>(R.id.ivIcon)
        btnCancel.text = getString(R.string.cancel)
        btnOk.text = getString(R.string.delete)
        title.text = getString(R.string.please_note)
        ivIcon.setImageResource(R.drawable.ic_delete_rounded_red)
        msg.text = getString(R.string.are_you_sure_you_want_to_delete_profile)
        btnOk.setOnClickListener {
            deleteDialog.dismiss()
//            viewModel.isLoading.value = true
//            viewModel.permanentlyDeleteAccount(session?.user?.userId)
        }
        btnCancel.setOnClickListener {
            deleteDialog.dismiss()
        }
    }

    private fun validate() {
        when {
            viewModel.userName.get().isNullOrBlank() -> {
                showToastShort(getString(R.string.err_enter_username))
            }

            else -> {
                viewModel.isLoading.value = true
                currentPin = ""
                newPin = ""
                (context as ActivityBase).session?.user?.userId?.let { /*viewModel.updateProfile(it)*/ }
            }
        }
    }

    override fun onResume() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            Constants.OLD_PIN
        )
            ?.observe(
                viewLifecycleOwner
            ) {
                if (!it.isNullOrBlank()) {
                    currentPin = it
                    val bundle = Bundle()
                    findNavController().previousBackStackEntry?.savedStateHandle?.remove<String>(
                        Constants.OLD_PIN
                    )
                    bundle.putString(Constants.OLD_PIN, currentPin)
                    bundle.putString(Constants.NEW_PIN, newPin)
                    findNavController().navigate(R.id.to_change_pin, bundle)
                }
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            Constants.NEW_PIN
        )
            ?.observe(
                viewLifecycleOwner
            ) {
                if (!it.isNullOrBlank()) {
                    newPin = it
                    findNavController().previousBackStackEntry?.savedStateHandle?.remove<String>(
                        Constants.NEW_PIN
                    )
                    val bundle = Bundle()
                    bundle.putString(Constants.OLD_PIN, currentPin)
                    bundle.putString(Constants.NEW_PIN, newPin)
                    findNavController().navigate(R.id.to_change_pin, bundle)
                }
            }
        super.onResume()
    }

    private fun updateUser(data: User) {

//        val mDatabaseRef = AppClass.mDBReference

        val params: MutableMap<String, Any> = HashMap()
        params["email"] = data.email.toString()
        params["name"] = data.username.toString()

        /*        mDatabaseRef.child(Constants.TABLE_USERS).child(data.userId.toString())
                    .updateChildren(params)*/
    }

    private fun deleteUser(data: User?) {
//        val mDatabaseRef = AppClass.mDBReference

        val params: MutableMap<String, Any> = HashMap()
        params["isDeleted"] = true

//        mDatabaseRef.child(Constants.TABLE_USERS).child(data?.userId.toString())
//            .updateChildren(params)
//
//        //remove scheduled message
//        mDBReference?.child(Constants.TABLE_CHAT)?.child(Constants.TABLE_SCHEDULE)
//            ?.child(session?.user?.userId.toString())?.removeValue()
    }


}