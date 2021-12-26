package gordon.lab.searchuser.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import gordon.lab.searchuser.core.AsyncDelegate
import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.customized.protocol.uiState
import gordon.lab.searchuser.customized.ui.userdetail.UserDetailState
import gordon.lab.searchuser.data.repository.UserDetailRepository
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor( private val repository: UserDetailRepository,private val asyncDelegate : AsyncDelegate) :
    BaseViewModel<MainEvent, UserDetailViewModel.State>(asyncDelegate) {

    data class State(
        val userDetailState: UserDetailState
    ) : uiState

    override fun onInitState(): State {
        return State(UserDetailState.Idle)
    }

    override fun onHandleEvent(event: MainEvent) {
        when (event) {

            is MainEvent.FetchUserDetail -> {
                fetchUserDetail(event.userName)
            }
        }
    }

    fun fetchUserDetail(username:String){
         asyncDelegate.ioJob {
             setState { copy(userDetailState = UserDetailState.Loading()) }
             try{
                 val result = repository.getUserDetail(username)
                 setState { copy(userDetailState = UserDetailState.Fetched(result)) }
            }catch (e:Exception){
                setState { copy(userDetailState = UserDetailState.Error(e.localizedMessage)) }
            }
        }
    }
}