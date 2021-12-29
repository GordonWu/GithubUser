package gordon.lab.searchuser.core.network

import gordon.lab.searchuser.data.repository.UserDetailRepository
import gordon.lab.searchuser.data.repository.UserListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class GithubApiTest: KoinTest {

//    @MockK
//    private lateinit var listRepo: UserListRepository
//
//
//    private lateinit var client: RetrofitClient
//
//    @MockK(relaxed = true)
//    private lateinit var detailRepo: UserDetailRepository

//    @Before
//    fun setup() {
//        MockKAnnotations.init(this)
//    }



    val listRepo by inject<UserListRepository>()
    val detailRepo by inject<UserDetailRepository>()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
         modules(module{
            factory { provideOkHttpClient(get()) }
            factory { GithubApi(get()) }
            factory { provideLoggingInterceptor() }
            single { provideRetrofit(get()) }

            single { UserDetailRepository(get()) }
            single { UserListRepository(get()) }
        })
    }

    @Test
    fun getUserList()   {
//        listRepo = UserListRepository(RetrofitClient.GithubApiProvides())
        //init call
        var data = runBlocking {
            listRepo.getUserList()
        }

        //test first page
        assert( data.size == 30)
        println("first page first id::${data[0].id}")
        assert( data[0].id == 1)

        // next page call
        data = runBlocking {
            listRepo.getUserList()
        }
        //test second page
        assert(data.size == 30)
        println("second page first id::${data[0].id}")
        assert(data[0].id != 1)
    }

    @Test
    fun getUserDetail(){
//        detailRepo = UserDetailRepository(RetrofitClient.GithubApiProvides())
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
