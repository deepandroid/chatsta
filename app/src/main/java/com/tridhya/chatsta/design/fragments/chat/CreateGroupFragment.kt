package com.tridhya.chatsta.design.fragments.chat

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.chatsta.R
import com.tridhya.chatsta.databinding.FragmentCreateGroupBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible

class CreateGroupFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentCreateGroupBinding
    private var page: Int = 1
//    private val viewModel by lazy { FeedViewModel(requireContext()) }
//    private var mDBReference: DatabaseReference? = null
    /* private val selectGroupMemberAdapter by lazy {
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
            binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
//            viewModel.isLoading.value = true
            getSearchedList("")
        }
        return binding.root
    }

    private fun getSearchedList(searchText: String) {
        /*viewModel.isLoading.value = true
        viewModel.myConnections(
            searchText.trim(),
            page
        )*/
        binding.tvNoConnections.visible()
        binding.rvUsers.gone()
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
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.etSearch.text.toString())
            }
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
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvUsers.adapter = selectGroupMemberAdapter
        binding.rvUsers.itemAnimator?.changeDuration = 0

        binding.rvSelectedUsers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvSelectedUsers.adapter = selectedConnectionsAdapter

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
                /*if (selectedConnectionsAdapter.getList().size > 1) {
                    createNewGroup(selectedConnectionsAdapter.getList())
                } else {
                    showToastShort(getString(R.string.create_group_error))
                }*/
            }
        }
    }

    /*    private fun createNewGroup(selectedUserList: ArrayList<User>) {
            val list = arrayListOf<UserData>()
            val threadId = mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_GROUPS)?.push()?.key

            val me = UserData()
            me.id = session?.user?.userId
            me.email = session?.user?.email
            if (session?.user?.images?.isNotEmpty() == true)
                me.image = session?.user?.images!![0].image
            me.name = session?.user?.username
            list.add(me)

            for (user in selectedUserList) {
                val member = UserData()
                member.id = user.userId
                member.email = user.email
                if (user.images?.isNotEmpty() == true)
                    member.image = user.images[0].image
                member.name = user.username
                list.add(member)
            }

            var groupName = ""

            val groupTable = mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_GROUPS)
                ?.child(threadId.toString())
            if (binding.tvGroupName.text.toString().trim().isNotBlank()) {
                groupName = binding.tvGroupName.text.toString().trim()
            } else {
                for (i in list) {
                    if (groupName.isNotBlank()) groupName += ", "
                    groupName += i.name
                }
            }
            val group =
                Group(
                    threadId,
                    groupName,
                    null,
                    session?.user?.userId,
                    binding.tvGroupName.text.toString().trim().isBlank()
                )
            groupTable?.setValue(group)

            val members = groupTable?.child("members")

            val mDBConversations =
                mDBReference?.child(Constants.TABLE_CHAT)?.child(Constants.TABLE_CONVERSATIONS)

            val threadRef = mDBReference
                ?.child(Constants.TABLE_CHAT)
                ?.child(Constants.TABLE_THREADS)
                ?.child(threadId.toString())

            val messageId = threadRef?.push()?.key
            val message = Message(
                messageId.toString(),
                session?.user?.userId.toString(),
                MessageType.CREATED_GROUP
            )
            threadRef?.child(messageId.toString())?.setValue(message)

            for (i in list) {
                members?.child(i.id.toString())
                    ?.setValue(Member(i.id.toString(), session?.user?.userId.equals(i.id)))

                //Injecting last message to conversation
                val conversation =
                    Conversation(
                        threadId,
                        true,
                        group.name.toString(),
                        group.image.toString(),
                        0,
                        message
                    )
                mDBConversations?.child(i.id.toString())?.child(threadId.toString())
                    ?.setValue(conversation)
            }

            hideProgressbar()
            val bundle = Bundle()
            bundle.putString("groupId", threadId)
            findNavController().navigate(R.id.to_group_chat, bundle)
        }*/

}