package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.customized.protocol.AsyncDelegate
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.customized.protocol.uiState
import gordon.lab.searchuser.customized.ui.userlist.UserListState
import gordon.lab.searchuser.data.model.UserList
import gordon.lab.searchuser.data.repository.UserListRepository


class UserListViewModel ( private val repository: UserListRepository, private val asyncApp : AsyncDelegate) :
    BaseViewModel<MainEvent, UserListViewModel.State>(asyncApp) {

    data class State(
        val userListState: UserListState
    ) : uiState

    override fun onInitState(): State {
        return State(UserListState.Idle)
    }

    override fun onHandleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.FetchUserList -> {
                if(repository.userListCache.isEmpty()){
                    fetchUserList()
                }else{
                    val userList = UserList()
                    userList.addAll(repository.userListCache)
                    setState { copy(userListState = UserListState.Fetched(userList)) }
                }
            }
        }
    }

    fun fetchUserList(){
        asyncApp.ioJob {
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