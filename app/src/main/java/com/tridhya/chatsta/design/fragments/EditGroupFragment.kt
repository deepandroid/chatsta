package com.tridhya.chatsta.design.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentEditGroupBinding
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible

class EditGroupFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditGroupBinding

    //    private var mDBReference: DatabaseReference? = null
    private var groupId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditGroupBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mDBReference = AppClass.mDBReference

        groupId = arguments?.getString("groupId").toString()

        getGroupName()

        binding.tvGroupName.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.ivClear.gone()
            } else {
                binding.ivClear.visible()
            }
        }

        binding.ivClear.setOnClickListener(this)
        binding.btnOk.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)

    }

    private fun getGroupName() {
//        mDBReference = AppClass.mDBReference
        /* mDBReference?.child(Constants.TABLE_CHAT)
             ?.child(Constants.TABLE_GROUPS)
             ?.child(groupId)
             ?.addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onDataChange(snapshot: DataSnapshot) {
                     val values = snapshot.value as HashMap<*, *>?

                     if (values != null) {
                         if (values.containsKey("defaultName") && !(values["defaultName"] as Boolean)) {
                             binding.tvGroupName.setText(values["name"].toString())
                         }
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {}

             })*/
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvCancel -> {
                preventDoubleClick(view)
                findNavController().navigateUp()
            }

            R.id.ivClear -> {
                preventDoubleClick(view)
                preventDoubleClick(view)
                binding.tvGroupName.setText("")
            }

            R.id.btnOk -> {
                preventDoubleClick(view)
                saveGroupName()
            }
        }
    }

    private fun saveGroupName() {
        val params: MutableMap<String, Any> = HashMap()

        if (binding.tvGroupName.text?.trim().isNullOrBlank()) {
            showToastLong("Group Name can't be blank")
            return
        } else {
            params["name"] = binding.tvGroupName.text?.trim().toString()
            params["defaultName"] = false
        }

//        mDBReference?.child(Constants.TABLE_CHAT)
//            ?.child(Constants.TABLE_GROUPS)
//            ?.child(groupId)
//            ?.updateChildren(params)

        findNavController().navigateUp()
    }

}