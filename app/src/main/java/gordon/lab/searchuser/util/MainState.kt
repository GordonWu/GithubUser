package gordon.lab.searchuser.util

import gordon.lab.searchuser.data.model.ApiResult
import gordon.lab.searchuser.data.model.UserDetail
import gordon.lab.searchuser.data.repository.UserListRepository

sealed class MainState{
    object Idle : MainState()
    data class  Loading(val isLoading:Boolean = true) : MainState()
    class DataFetched(var result: ApiResult, val isLoading:Boolean = false): MainState()
    class DetailFetched(var result: UserDetail, val isLoading:Boolean = false): MainState()
    data class Error(val error : String?,val isLoading:Boolean = false): MainState()
}
