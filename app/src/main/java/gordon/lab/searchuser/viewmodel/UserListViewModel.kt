package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.customized.protocol.AsyncDelegate
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.customized.ui.userlist.UserListState
import gordon.lab.searchuser.data.repository.UserListRepository


class UserListViewModel ( private val repository: UserListRepository, private val asyncApp : AsyncDelegate) :
    BaseViewModel<MainEvent, UserListState>(asyncApp) {

    override fun onInitState(): UserListState {
        return UserListState.Idle
    }

    override fun onHandleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.FetchUserList -> {
                if(repository.userListCache.isEmpty()){
                    fetchUserList()
                }
            }
        }
    }

    fun fetchUserList(){
        asyncApp.ioJob {
            setState { UserListState.Loading() }
            try{
                val result = repository.getUserList()
                setState { UserListState.Fetched(result) }
             }catch (e:Exception){
                setState {  UserListState.Error(e.localizedMessage) }
            }
        }
    }
}