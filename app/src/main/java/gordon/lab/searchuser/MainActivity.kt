package gordon.lab.searchuser

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gordon.lab.searchuser.customized.adapter.SearchUserAdapter
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.databinding.ActivityMainBinding
import gordon.lab.searchuser.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userListAdapter:SearchUserAdapter
    private val viewModel: MainActivityViewModel by viewModels()
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView(){
        binding.btnSend.setOnClickListener(onBtnSendClickListener)
        binding.btnClean.setOnClickListener(onBtnCleanClickListener)

        binding.userList.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity,DividerItemDecoration.VERTICAL))
            userListAdapter = SearchUserAdapter()
            adapter = userListAdapter
        }

    }

    private fun initViewModel(){
        viewModel.currentName.observe(this, currentNameObserver)

        //livedata用
//        viewModel.searchLiveDataResult.observe(this, searchLiveDataResultObserver)
    }

    //作為flow測試用
    private fun search(query: String, page:Int) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getUserListFlow(query,page).collect{
                userListAdapter.submitData(lifecycle,it)
            }
        }
    }


    // Event Handlers
    //

    private val onBtnSendClickListener = View.OnClickListener {
        if (!TextUtils.isEmpty(binding.etSearchUser.text.toString())){
            val user = binding.etSearchUser.text.toString()
            viewModel.setCurrentName(user)
            search(user,1)
        }
    }

    private val onBtnCleanClickListener = View.OnClickListener {
        viewModel.currentName.value = ""
    }

    private val currentNameObserver = Observer<String> {
        binding.etSearchUser.setText(it)
    }

    //livedata用
//    private val searchLiveDataResultObserver = Observer<PagingData<UserItems>> {
//        userListAdapter.submitData(lifecycle,it)
//    }
}