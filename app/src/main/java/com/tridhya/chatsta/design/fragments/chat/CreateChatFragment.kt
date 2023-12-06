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
import com.tridhya.chatsta.databinding.FragmentCreateChatBinding
import com.tridhya.chatsta.design.fragments.BaseFragment
import com.tridhya.chatsta.extensions.gone
import com.tridhya.chatsta.extensions.visible

class CreateChatFragment : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentCreateChatBinding

    /*private val adapter by lazy {
        UserListAdapter(
            context = requireContext(),
            listener = this,
            loadMoreListener = this
        )
    }*/
//    private var mDatabaseRef: DatabaseReference? = null
    private var page: Int = 1
//    private val viewModel by lazy { FeedViewModel(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCreateChatBinding.inflate(inflater, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            page = 1
//            viewModel.isLoading.value = true
            getSearchedList("")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mDatabaseRef = AppClass.mDBReference
        initViews()
        setObservers()
        binding.ivClear.setOnClickListener(this)
        binding.llCreateGroup.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)

        binding.etSearch.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.ivClear.gone()
            } else {
                binding.ivClear.visible()
            }
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
//            adapter.clearList()
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.etSearch.text.toString())
            }
            false
        }
    }

    private fun getSearchedList(searchText: String) {
        /* viewModel.isLoading.value = true
         viewModel.myConnections(
             searchText.trim(),
             page
         )*/
        binding.tvNoConnections.visible()
        binding.rvUsers.gone()
    }

    private fun setObservers() {
        /*viewModel.responseMyConnections.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
            if (it.connections.isNullOrEmpty() && page == 1) {
                adapter.clearList()
                binding.tvNoConnections.visible()
                binding.rvUsers.gone()
            } else {
                binding.rvUsers.visible()
                binding.tvNoConnections.gone()
                binding.rvUsers.adapter = adapter
                if (page == 1) {
                    adapter.setList(it.connections)
                } else {
                    adapter.addList(it.connections)
                }
            }
        }*/
    }

    private fun initViews() {
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvUsers.adapter = adapter
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivClose -> {
                preventDoubleClick(view)
                findNavController().popBackStack()
            }

            R.id.ivClear -> {
                preventDoubleClick(view)
                binding.etSearch.setText("")
//                adapter.clearList()
//                viewModel.isRefreshing.value = true
                page = 1
                getSearchedList(binding.etSearch.text.toString())
            }

            R.id.llCreateGroup -> {
                preventDoubleClick(view)
                findNavController().navigate(R.id.to_create_group)
            }
        }
    }

    /* override fun onItemClicked(position: Int, user: User) {
         val bundle = Bundle()
         bundle.putString("userId", user.userId)
         findNavController().popBackStack()
         findNavController().navigate(R.id.to_chat, bundle)
     }

     override fun onItemSelect(position: Int, user: User) {

     }

     override fun onLoadMore() {
         ++page
         getSearchedList(binding.etSearch.text.toString())
     }*/
}