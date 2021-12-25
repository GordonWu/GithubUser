package gordon.lab.searchuser.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: UserListRepository) : ViewModel() {

    var ui: CoroutineDispatcher = Dispatchers.Main
    var io: CoroutineDispatcher =  Dispatchers.IO
    var background: CoroutineDispatcher = Dispatchers.Default

    var userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() =_state

    init {
        handleIntent()
    }

    fun ViewModel.uiJob(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(ui) {
            block()
        }
    }

    fun ViewModel.ioJob(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(io) {
            block()
        }
    }

    fun ViewModel.backgroundJob(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(background) {
            block()
        }
    }

    private fun handleIntent(){
        ioJob {
            userIntent.consumeAsFlow().collect {
                when(it){
                    is MainIntent.FetchUserList -> {
                        if (repository.userListCache.isEmpty()) {
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
                 MainState.Error(e.localizedMessage)
            }
        }
    }
}