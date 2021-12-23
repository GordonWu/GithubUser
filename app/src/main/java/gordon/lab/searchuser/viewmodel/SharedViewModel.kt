package gordon.lab.searchuser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.data.repository.SearchUserRepository
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: SearchUserRepository) : ViewModel() {

    var userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() =_state

    init {
        handleIntent()
    }

    private fun handleIntent(){
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when(it){
                    is MainIntent.FetchUserList -> fetchUserList(it.userID)
//                    is MainIntent.FetchUserDetail -> fetchUserDetail()
                }
            }
        }
    }

    private fun fetchUserList(since:Int){
        viewModelScope.launch {
            _state.value = MainState.Loading()
            _state.value = try{
                MainState.DataFetched(repository.getUserList(since))
            }catch (e:Exception){
                Log.e("gw",e.localizedMessage)
                MainState.Error(e.localizedMessage)
            }
        }
    }

//    private fun fetchUserDetail(username:String){
//        viewModelScope.launch {
//            _state.value = MainState.Loading()
//            _state.value = try{
//                MainState.DataFetched(repository.getUserDetail(username))
//            }catch (e:Exception){
//                Log.e("gw",e.localizedMessage)
//                MainState.Error(e.localizedMessage)
//            }
//        }
//    }
}