package gordon.lab.searchuser.data.repository

import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.data.model.UserList

class UserListRepository (private val api: GithubApi) {

    private val mUserListCache: MutableList<UserItems> = mutableListOf()
    val userListCache: List<UserItems> get() = mUserListCache

    suspend fun getUserList():  UserList  {
        val lastId = if (mUserListCache.isNotEmpty()) {
            mUserListCache.last().id
        } else null

        return api.getUserList(lastId?:0)
            .also { mUserListCache.addAll(it) }
    }
}

