package gordon.lab.searchuser.data.repository

import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.data.model.UserDetail
import javax.inject.Inject

class UserDetailRepository @Inject constructor (private val api: GithubApi) {
    suspend fun getUserDetail(username :String): UserDetail {
        return api.getUserDetail(username)
    }
}