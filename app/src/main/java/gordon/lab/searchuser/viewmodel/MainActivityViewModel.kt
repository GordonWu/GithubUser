package gordon.lab.searchuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import gordon.lab.searchuser.data.UserItems
import gordon.lab.searchuser.repository.SearchUserRepository
import kotlinx.coroutines.flow.Flow


class MainActivityViewModel (private val repository: SearchUserRepository) : ViewModel() {

    // Create a LiveData with a String
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val userList: MutableLiveData<PagingData<UserItems>> by lazy{
        MutableLiveData<PagingData<UserItems>>()
    }
    private var currentQueryPage: Int = 1

    private var currentSearchResult: Flow<PagingData<UserItems>>? = null


    fun getUserListLiveData(query:String,page:Int) {
        val response = repository.getUserListLiveData(query,page).cachedIn(viewModelScope)
        userList.value = response.value
    }

    fun getUserListFlow(query:String, page:Int): Flow<PagingData<UserItems>> {
        val lastResult = currentSearchResult
        if (query == currentName.value && lastResult != null) {
            return lastResult
        }
        currentName.value = query
        currentQueryPage = page

        val newResult: Flow<PagingData<UserItems>> = repository.getUserListFlow(query,page)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}