package gordon.lab.searchuser.customized.ui.userdetail

import gordon.lab.searchuser.customized.protocol.uiState
import gordon.lab.searchuser.data.model.UserDetail

sealed class UserDetailState : uiState {
    object Idle : UserDetailState()
    data class  Loading(val isLoading:Boolean = true) : UserDetailState()
    class Fetched(var result: UserDetail , val isLoading:Boolean = false): UserDetailState()
    data class Error(val error : String?,val isLoading:Boolean = false): UserDetailState()
}
