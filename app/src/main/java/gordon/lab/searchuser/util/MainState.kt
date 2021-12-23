package gordon.lab.searchuser.util

import gordon.lab.searchuser.data.model.UserList

sealed class MainState{
    object Idle : MainState()
    data class  Loading(val isLoading:Boolean = true) : MainState()
    data class LoadMore(val isLoading:Boolean = true): MainState()
    class DataFetched(val userList : UserList, val isLoading:Boolean = false): MainState()
    data class Error(val error : String?,val isLoading:Boolean = false): MainState()
}
