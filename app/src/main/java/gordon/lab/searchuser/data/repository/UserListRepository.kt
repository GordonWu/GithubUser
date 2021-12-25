package gordon.lab.searchuser.data.repository

import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.data.model.ApiResult
import gordon.lab.searchuser.data.model.UserItems
import javax.inject.Inject

class UserListRepository @Inject constructor (private val api: GithubApi) {

    private val mUserListCache: MutableList<UserItems> = mutableListOf()
    val userListCache: List<UserItems> get() = mUserListCache

    suspend fun getUserList(): ApiResult {
        val lastId = if (mUserListCache.isNotEmpty()) {
            mUserListCache.last().id
        } else null

        return ApiResult(api.getUserList(lastId?:0)
            .also { mUserListCache.addAll(it) })
    }
}