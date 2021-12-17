package gordon.lab.searchuser.customized.protocol

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import gordon.lab.searchuser.data.UserItems
import kotlinx.coroutines.flow.Flow

interface ISearchUserRepository {
  fun getUserListFlow(query: String, page: Int): Flow<PagingData<UserItems>>
  fun getUserListLiveData(query: String, page: Int): LiveData<PagingData<UserItems>>
}