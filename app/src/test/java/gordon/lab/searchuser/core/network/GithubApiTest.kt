package gordon.lab.searchuser.core.network

import gordon.lab.searchuser.core.SearchUserAppModule
import gordon.lab.searchuser.data.repository.UserDetailRepository
import gordon.lab.searchuser.data.repository.UserListRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GithubApiTest {

    @MockK
    private lateinit var listRepo: UserListRepository

    @MockK
    private lateinit var detailRepo: UserDetailRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getUserList()   {
        listRepo = UserListRepository(SearchUserAppModule.GithubApiProvides())
        //init call
        var data = runBlocking {
            listRepo.getUserList().userList
        }

        //test first page
        assert( data.size == 30)
        println("first page first id::${data[0].id}")
        assert( data[0].id == 1)

        // next page call
        data = runBlocking {
            listRepo.getUserList().userList
        }
        //test second page
        assert(data.size == 30)
        println("second page first id::${data[0].id}")
        assert(data[0].id != 1)
    }

    @Test
    fun getUserDetail(){
        detailRepo = UserDetailRepository(SearchUserAppModule.GithubApiProvides())
        //init call
        val data = runBlocking {
            detailRepo.getUserDetail("GordonWu")
        }
        println("user deilt:${data}")
        assert(data.login == "GordonWu")
        assert(data.id == 6059222)
        assert(data.location == "Taiwan. Taipei")
    }
}
