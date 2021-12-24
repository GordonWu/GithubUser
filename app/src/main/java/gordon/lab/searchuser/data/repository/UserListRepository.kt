package gordon.lab.searchuser.data.repository

import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.data.model.UserItems
import javax.inject.Inject

class UserListRepository @Inject constructor (private val api: GithubApi) {

    fun getAPI() = api

    private val mUserInfoCache: MutableList<UserItems> = mutableListOf()
    val userInfoCache: List<UserItems> get() = mUserInfoCache

    data class ApiResult(
        val userInfoList: List<UserItems>
    )

    suspend fun getUserList(): ApiResult {
        val lastId = if (mUserInfoCache.isNotEmpty()) {
            mUserInfoCache.last().id
        } else null

        return ApiResult(api.getUserList(lastId?:0)
            .also { mUserInfoCache.addAll(it) })
    }
}