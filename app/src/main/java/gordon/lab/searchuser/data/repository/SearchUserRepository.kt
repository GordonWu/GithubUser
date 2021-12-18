package gordon.lab.searchuser.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.customized.protocol.ISearchUserRepository
import javax.inject.Inject

class SearchUserRepository @Inject constructor (private val api: GithubApi) :ISearchUserRepository{

    override fun getUserListFlow(query: String, page: Int) = Pager(PagingConfig(page)) {
        SearchUserPagingSource(api, query)
    }.flow

    override fun getUserListLiveData( query: String, page: Int ) = Pager(PagingConfig(page) ) {
        SearchUserPagingSource(api, query)
    }.liveData
}