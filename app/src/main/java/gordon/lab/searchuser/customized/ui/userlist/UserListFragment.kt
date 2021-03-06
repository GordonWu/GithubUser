package gordon.lab.searchuser.customized.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import gordon.lab.searchuser.customized.adapter.UserListAdapter
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.databinding.FragmentUserListBinding
import gordon.lab.searchuser.util.NavControllerHelper
import gordon.lab.searchuser.viewmodel.UserListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserListFragment: Fragment() {

    private val viewModel: UserListViewModel by viewModel()
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
                viewModel.setEvent(MainEvent.FetchUserList)
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
        userListAdapter.setLoadMore(viewModel::fetchUserList)
        userListAdapter.setOnItemClick(::onUserItemClick)
        userList.layoutManager = LinearLayoutManager(context)
        userList.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
    }

    private fun FragmentUserListBinding.initViewModelObserve(){
        lifecycleScope.launch {
            viewModel.viewState.collect { it ->
                when(it){
                    is UserListState.Idle->{
                        //do nothing~
                    }
                    is UserListState.Loading->{
                        progressBar.isVisible = it.isLoading
                    }
                    is UserListState.Fetched->{
                        it.result.apply {
                            userListAdapter.setDataModel(this)
                            progressBar.isVisible =  it.isLoading
                        }
                    }
                    is UserListState.Error->{
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                        progressBar.isVisible =  it.isLoading
                    }
                }
            }
        }
    }
}