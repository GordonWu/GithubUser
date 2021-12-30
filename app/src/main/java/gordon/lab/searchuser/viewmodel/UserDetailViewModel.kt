package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.customized.protocol.AsyncDelegate
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.customized.ui.userdetail.UserDetailState
import gordon.lab.searchuser.data.repository.UserDetailRepository


class UserDetailViewModel(private val repository: UserDetailRepository, private val asyncApp : AsyncDelegate) :
    BaseViewModel<MainEvent, UserDetailState>(asyncApp) {


    override fun onInitState(): UserDetailState {
        return UserDetailState.Idle
    }

    override fun onHandleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.FetchUserDetail -> {
                fetchUserDetail(event.userName)
            }
        }
    }

    fun fetchUserDetail(username:String){
        asyncApp.ioJob {
             setState { UserDetailState.Loading() }
             try{
                 val result = repository.getUserDetail(username)
                 setState { UserDetailState.Fetched(result) }
            }catch (e:Exception){
                setState { UserDetailState.Error(e.localizedMessage) }
            }
        }
    }


}