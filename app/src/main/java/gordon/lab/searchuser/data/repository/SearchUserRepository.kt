package gordon.lab.searchuser.data.repository

import gordon.lab.searchuser.core.network.GithubApi
import javax.inject.Inject

class SearchUserRepository @Inject constructor (private val api: GithubApi)  {
    suspend fun getUserList(since:Int) = api.getUserList(since)
    suspend fun getUserDetail(username:String) = api.getUserDetail(username)
}