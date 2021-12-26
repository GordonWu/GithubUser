package gordon.lab.searchuser.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import gordon.lab.searchuser.core.AsyncDelegate
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.customized.protocol.uiState
import gordon.lab.searchuser.customized.ui.userlist.UserListState
import gordon.lab.searchuser.data.repository.UserListRepository
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor( private val repository: UserListRepository, private val asyncDelegate : AsyncDelegate) :
    BaseViewModel<MainEvent, UserListViewModel.State>(asyncDelegate) {

    data class State(
        val userListState: UserListState
    ) : uiState

    override fun onInitState(): State {
        return State(UserListState.Idle)
    }

    override fun onHandleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.FetchUserList -> {
                if(repository.userListCache.isEmpty())
                    fetchUserList()
            }
        }
    }

    fun fetchUserList(){
        asyncDelegate.ioJob {
            setState { copy(userListState = UserListState.Loading()) }
            try{
                val result = repository.getUserList()
                setState { copy(userListState = UserListState.Fetched(result)) }
             }catch (e:Exception){
                setState { copy(userListState = UserListState.Error(e.localizedMessage)) }
            }
        }
    }
}