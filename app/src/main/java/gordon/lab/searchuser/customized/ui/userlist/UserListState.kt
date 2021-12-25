package gordon.lab.searchuser.customized.ui.userlist

import gordon.lab.searchuser.customized.protocol.uiState
import gordon.lab.searchuser.data.model.ApiResult

sealed class UserListState: uiState {
    object Idle : UserListState()
    data class  Loading(val isLoading:Boolean = true) : UserListState()
    class Fetched(var result: ApiResult, val isLoading:Boolean = false): UserListState()
    data class Error(val error : String?,val isLoading:Boolean = false): UserListState()
}
