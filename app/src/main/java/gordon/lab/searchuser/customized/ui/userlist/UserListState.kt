package gordon.lab.searchuser.customized.ui.userlist

import gordon.lab.searchuser.customized.protocol.UiState
import gordon.lab.searchuser.data.model.UserList

sealed class UserListState: UiState {
    object Idle : UserListState()
    data class  Loading(val isLoading:Boolean = true) : UserListState()
    class Fetched(var result: UserList , val isLoading:Boolean = false): UserListState()
    data class Error(val error : String?,val isLoading:Boolean = false): UserListState()
}
