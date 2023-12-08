package com.tridhya.chatsta.design.fragments.chat.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentAddMemberToGroupBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible
import java.lang.reflect.Member

class AddMemberToGroupFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddMemberToGroupBinding
    private var page: Int = 1

    //    private val viewModel by lazy { FeedViewModel(requireContext()) }
    private var threadId: String? = null

    //    private var mDBReference: DatabaseReference? = null
//    private var mThreadReference: DatabaseReference? = null
//    private var groupValueEventListener: ValueEventListener? = null
    var memberList = arrayListOf<Member>()
    /*private val selectGroupMemberAdapter by lazy {
        SelectGroupMemberAdapter(
            mContext = requireContext(),
            listener = this,
            loadMoreListener = this,
        )
    }
    private val selectedConnectionsAdapter by lazy {
        SelectedConnectionsAdapter(
            listener = this,
        )
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentAddMemberToGroupBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            val arguments = arguments
            if (arguments != null) {
                threadId = arguments.getString("groupId")
            }
            page = 1
//            viewModel.isLoading.value = true
            getSearchedList("")
        }
        return binding.root
    }

    private fun getSearchedList(searchText: String) {
//        viewModel.isLoading.value = true
        /* viewModel.myConnections(
             searchText.trim(),
             page
         )*/
    }

    private fun initListeners() {
        /* groupValueEventListener = object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 val values = dataSnapshot.value as HashMap<*, *>?
                 if (values != null) {

                     memberList = arrayListOf()

                     dataSnapshot.child("members").children.forEach {
                         val member = it.getValue(Member::class.java)
                         member?.let { it1 -> memberList.add(it1) }
                     }

                     val member = dataSnapshot.child("members")
                         .child(session?.user?.userId.toString())
                         .getValue(Member::class.java)

                     if (member == null) {
                         findNavController().popBackStack()//will remove user from screen if removed from group while ongoing chat
                     } else {
                         val cal = Calendar.getInstance()
                         if (member.lastDeleted != null) {
                             cal.timeInMillis = member.lastDeleted!!
                         } else {
                             cal.timeInMillis = member.doj
                         }
                     }
                 }
             }

             override fun onCancelled(databaseError: DatabaseError) {
                 showToastLong(databaseError.message)
             }
         }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mDBReference = AppClass.mDBReference
        initViews()
        setObservers()
        binding.ivClear.setOnClickListener(this)
        binding.btnOk.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)

        binding.etSearch.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.ivClear.gone()
            } else {
                binding.ivClear.visible()
            }
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
//            selectGroupMemberAdapter.clearList()
//            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
//                viewModel.isRefreshing.value = true
//                page = 1
//                getSearchedList(binding.etSearch.text.toString())
//            }
            false
        }
    }

    private fun setObservers() {
        /*        viewModel.responseMyConnections.observe(viewLifecycleOwner) {
                    viewModel.isLoading.value = false
                    if (it.connections.isNullOrEmpty() && page == 1) {
                        selectGroupMemberAdapter.clearList()
                        binding.tvNoConnections.visible()
                        binding.rvUsers.gone()
                    } else {
                        val list = arrayListOf<User>()
                        val selected = selectedConnectionsAdapter.getList()
                        for (i in it.connections!!) {
                            if (checkInMembers(i.userId)) continue
                            for (j in selected) {
                                if (i.userId == j.userId) {
                                    i.isSelected = true
                                    break
                                }
                            }
                            list.add(i)
                        }
                        binding.rvUsers.visible()
                        binding.tvNoConnections.gone()
                        binding.rvUsers.adapter = selectGroupMemberAdapter
                        if (page == 1) {
                            selectGroupMemberAdapter.setList(list)
                        } else {
                            selectGroupMemberAdapter.addList(list)
                        }
                    }
                }*/
    }

    private fun initViews() {
        initListeners()
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvUsers.adapter = selectGroupMemberAdapter
        binding.rvUsers.itemAnimator?.changeDuration = 0

        binding.rvSelectedUsers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvSelectedUsers.adapter = selectedConnectionsAdapter
        checkInternetAndInitialize()

    }

    private fun checkInternetAndInitialize() {

        /*mThreadReference = mDBReference
            ?.child(Constants.TABLE_CHAT)
            ?.child(Constants.TABLE_THREADS)
            ?.child(threadId.toString())

        groupValueEventListener?.let {
            mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_GROUPS)
                ?.child(threadId.toString())
                ?.addListenerForSingleValueEvent(it)
        }*/
    }

    override fun onStop() {
        super.onStop()

//        groupValueEventListener?.let {
//            mDBReference
//                ?.child(Constants.TABLE_CHAT)
//                ?.child(Constants.TABLE_GROUPS)
//                ?.child(threadId.toString())
//                ?.removeEventListener(it)
//        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvCancel -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.ivClear -> {
                preventDoubleClick(view)
                preventDoubleClick(view)
                binding.etSearch.setText("")
//                selectGroupMemberAdapter.clearList()
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.etSearch.text.toString())
            }

            R.id.btnOk -> {
                preventDoubleClick(view)
                /*if (selectedConnectionsAdapter.getList().size > 0) {
                    addMembersToTheGroup(selectedConnectionsAdapter.getList())
                } else {*/
                showToastShort(getString(R.string.create_group_member_error))
//                }
            }
        }
    }


}