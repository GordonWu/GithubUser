package gordon.lab.searchuser.customized.ui.userlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import gordon.lab.searchuser.databinding.FragmentUserListBinding
import gordon.lab.searchuser.util.NavControllerHelper
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import gordon.lab.searchuser.customized.adapter.UserListAdapter
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import gordon.lab.searchuser.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UserListFragment: Fragment() {

    private val sharedViewModel:SharedViewModel by activityViewModels()
    private var binding: FragmentUserListBinding ?= null
    private val navHelper: NavControllerHelper = NavControllerHelper()
    private var userListAdapter = UserListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            initUserRecycler()
            initViewModelObserve()
            lifecycleScope.launch {
                sharedViewModel.userIntent.send(MainIntent.FetchUserList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navHelper.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun onUserItemClick(username: String) {
        navHelper.navigate(findNavController(), UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(username))
    }

    private fun FragmentUserListBinding.initUserRecycler() {
        userList.adapter = userListAdapter
        userListAdapter.setLoadMore(sharedViewModel::fetchUserList)
        userListAdapter.setOnItemClick(::onUserItemClick)
        userList.layoutManager = LinearLayoutManager(context)
        userList.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
    }

    private fun FragmentUserListBinding.initViewModelObserve(){
        lifecycleScope.launch {
            sharedViewModel.state.collect {
                when(it){
                    is MainState.Idle->{
                        //do nothing~
                    }
                    is MainState.Loading->{
                        progressBar.isVisible = it.isLoading
                    }
                    is MainState.DataFetched->{
                        userListAdapter.setDataModel(it.result.userList)
                        progressBar.isVisible = it.isLoading
                    }
                    is MainState.Error->{
                        Toast.makeText(context,it.error, Toast.LENGTH_LONG).show()
                        progressBar.isVisible = it.isLoading
                    }
                }
            }
        }
    }
}