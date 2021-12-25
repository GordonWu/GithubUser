package gordon.lab.searchuser.core.network

import gordon.lab.searchuser.core.SearchUserAppModule
import gordon.lab.searchuser.data.repository.UserListRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GithubApiTest {

    @MockK
    private lateinit var repo: UserListRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getUserList()   {
        repo = UserListRepository(SearchUserAppModule.GithubApiProvides())
        //init call
        var data = runBlocking {
            repo.getUserList().userList
        }

        //test first page
        assert( data.size == 30)
        println("first page first id::${data[0].id}")
        assert( data[0].id == 1)

        // next page call
        data = runBlocking {
            repo.getUserList().userList
        }
        //test second page
        assert(data.size == 30)
        println("second page first id::${data[0].id}")
        assert(data[0].id != 1)
    }

    @Test
    fun getUserDetail(){
        val data = runBlocking {
            SearchUserAppModule.GithubApiProvides().getUserDetail("GordonWu")
        }
        assert(data.login == "GordonWu")
        assert(data.id == 6059222)
        assert(data.location == "Taiwan. Taipei")
    }
}
