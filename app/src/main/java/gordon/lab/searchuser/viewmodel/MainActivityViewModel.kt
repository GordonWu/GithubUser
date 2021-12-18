package gordon.lab.searchuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.data.repository.SearchUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: SearchUserRepository) : ViewModel() {

    val currentName :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    // flow test
    private var searchFlowResult: Flow<PagingData<UserItems>>? = null
    fun getUserListFlow(query:String, page:Int): Flow<PagingData<UserItems>> {
        val lastResult = searchFlowResult
        if (query == currentName.value && lastResult != null) {
            return lastResult
        }
        currentName.value = query

        val newResult = repository.getUserListFlow(query, page).cachedIn(viewModelScope)
        searchFlowResult = newResult
        return newResult
    }

    //livedata
    var searchLiveDataResult = currentName.switchMap {
        repository.getUserListLiveData(it,1).cachedIn(viewModelScope)
    }

    fun setCurrentName(query:String){
        currentName.postValue(query)
    }

}