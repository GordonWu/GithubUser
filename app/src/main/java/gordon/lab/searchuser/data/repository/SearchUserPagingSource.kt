package gordon.lab.searchuser.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.data.model.UserItems
import retrofit2.HttpException
import java.io.IOException

class SearchUserPagingSource(private val api: GithubApi, private val queryUser:String ):PagingSource<Int, UserItems>() {

    override fun getRefreshKey(state: PagingState<Int, UserItems>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserItems> {
        val pageIndex = params.key ?: 1
        return try {
            val data = api.getUserList(q = queryUser,sort = "desc",order = "indexed", per_page =  30, page = pageIndex)
            LoadResult.Page(
                data = data.UserItems!!,
                prevKey = if (pageIndex == 1) null else pageIndex -1 ,
                nextKey =  if (data.UserItems.isEmpty()) null else pageIndex + 1,
                )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}