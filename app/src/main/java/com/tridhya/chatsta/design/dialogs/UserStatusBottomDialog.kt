package com.tridhya.chatsta.design.dialogs

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.tridhya.chatsta.Model.User
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.databinding.LayoutUserStatusBinding
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment
import com.tridhya.chatsta.design.viewModel.chat.ChatViewModel
import com.tridhya.chatsta.enum.chat.UserStatus

class UserStatusBottomDialog :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutUserStatusBinding
    private val viewModel by lazy { ChatViewModel(requireContext()) }
    private var status = "Offline"
    lateinit var user: User
    private var isOnline: UserStatus = UserStatus.OFFLINE
//    private var mDatabaseRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutUserStatusBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.viewModel = viewModel
        user = (context as ActivityBase).session?.user!!
//        mDatabaseRef = AppClass.mDBReference
        initViews()
        setObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.ivBack.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)

        binding.etStatus.singleLine = true
        binding.etStatus.editText.maxLines = 1
        binding.etStatus.editText.ellipsize = TextUtils.TruncateAt.END
        binding.etChatStatus.editText.setOnClickListener {
            (context as ActivityBase).preventDoubleClick(it)
            if (binding.etChatStatus.value != null)
                status = viewModel.chatStatus.get().toString().lowercase().capitalize()

            activity?.supportFragmentManager?.let { it1 ->
                UserStatusPickerBottomDialog.newInstance(
                    status,
                    object : UserStatusPickerBottomDialog.OptionListener {
                        override fun onOptionSelected(view: View, selectedValue: String) {
                            viewModel.chatStatus.set(selectedValue.lowercase().capitalize())
                        }
                    }).show(it1, "dialog")
            }
        }
    }

    private fun setObservers() {

    }

    private fun initViews() {

        /* mDatabaseRef?.child(Constants.TABLE_USERS)?.child(user.userId.toString())
             ?.addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onDataChange(dataSnapshot: DataSnapshot) {
                     val values = dataSnapshot.value as java.util.HashMap<*, *>?
                     Log.e("DataSnapshot", dataSnapshot.value.toString())

                     if (values != null) {
                         if (values.containsKey("isOnline") && values["isOnline"] != null)
                             isOnline = UserStatus.valueOf(values["isOnline"].toString())
                         val userSetStatus =
                             if (values.containsKey("userSetStatus")) UserStatus.valueOf(values["userSetStatus"].toString()) else UserStatus.ONLINE
                         if (values.containsKey("image") && values["image"] != null)
                             GlideUtils(requireContext()).circleImage()
                                 .loadImage(values["image"].toString(), binding.ivProfileImage)
                         else
                             GlideUtils(requireContext()).circleImage()
                                 .loadImage(R.drawable.ic_app, binding.ivProfileImage)
                         if (values.containsKey("userSetStatus") && values["userSetStatus"] != null) {
                             binding.etChatStatus.editText.setText(
                                 values["userSetStatus"].toString().lowercase().capitalize()
                             )
                             isOnline = UserStatus.valueOf(values["userSetStatus"].toString())
                         } else {
                             binding.etChatStatus.editText.setText(
                                 values["isOnline"].toString().lowercase().capitalize()
                             )
                         }

                         if (values.containsKey("userTextStatus") && values["userTextStatus"] != null)
                             binding.etStatus.editText.setText(values["userTextStatus"].toString())

                         when (if (userSetStatus == UserStatus.ONLINE) isOnline else userSetStatus) {
                             UserStatus.ONLINE -> {
                                 binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_green)
                             }
                             UserStatus.BUSY -> {
                                 binding.ivProfileIndicator.setImageResource(R.drawable.ic_busy_status)
                             }
                             else -> {
                                 binding.ivProfileIndicator.setImageResource(R.drawable.v_ic_circle_red)
                             }
                         }
                     }
                 }

                 override fun onCancelled(databaseError: DatabaseError) {}
             })*/
        binding.etStatus.editText.onFocusChangeListener =
            View.OnFocusChangeListener { p0, selected ->
                binding.etStatus.llInputText.isSelected = selected
                binding.etStatus.tvTitle.isSelected = selected
            }

        binding.etStatus.editText.addTextChangedListener {
            if (!it.toString().isNullOrBlank()) {
                binding.etStatus.editText.isSelected = true
                binding.etStatus.tvTitle.isSelected = true
            }
        }
        binding.etChatStatus.editText.addTextChangedListener {
            if (!it.toString().isNullOrBlank()) {
                binding.etChatStatus.editText.isSelected = true
                binding.etChatStatus.tvTitle.isSelected = true
                binding.etChatStatus.ivDownArrow.isSelected = true
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                (context as ActivityBase).preventDoubleClick(view)
                dialog?.dismiss()
            }

            R.id.tvSave -> {
                (context as ActivityBase).preventDoubleClick(view)
                dialog?.dismiss()
                checkOrCreateNewUser(user)
            }
        }
    }

    private fun checkOrCreateNewUser(data: User) {
        val params: MutableMap<String, Any> = HashMap()
        if (!binding.etChatStatus.editText.text.isNullOrBlank() || !binding.etChatStatus.editText.text.isNullOrEmpty())
            params["userSetStatus"] = binding.etChatStatus.editText.text.toString().uppercase()
        else
            params["userSetStatus"] = UserStatus.ONLINE
        if (!binding.etStatus.editText.text.isNullOrBlank() || !binding.etStatus.editText.text.isNullOrEmpty())
            params["userTextStatus"] = binding.etStatus.editText.text.toString()
        else
            params["userTextStatus"] = ""
        params["isOnline"] = UserStatus.ONLINE
        params["lastSeen"] = System.currentTimeMillis()

        /* mDatabaseRef
             ?.child(Constants.TABLE_USERS)
             ?.child(data.userId.toString())
             ?.updateChildren(params)*/
    }
}