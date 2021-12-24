package gordon.lab.searchuser.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: UserListRepository) : ViewModel() {

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
                    is MainIntent.FetchUserList -> {
                        if (repository.userInfoCache.isEmpty()) {
                            fetchUserList()
                        }
                    }
                    is MainIntent.FetchUserDetail -> fetchUserDetail(it.userName)
                }
            }
        }
    }

    fun fetchUserList(){
        viewModelScope.launch {
            _state.value = MainState.Loading()
            _state.value = try{
                MainState.DataFetched(repository.getUserList())
            }catch (e:Exception){
                Log.e("gw",e.localizedMessage)
                MainState.Error(e.localizedMessage)
            }
        }
    }

    private fun fetchUserDetail(username:String){
        viewModelScope.launch {
            _state.value = MainState.Loading()
            _state.value = try{
                MainState.DetailFetched(repository.getAPI().getUserDetail(username))
            }catch (e:Exception){
                Log.e("gw",e.localizedMessage)
                MainState.Error(e.localizedMessage)
            }
        }
    }
}