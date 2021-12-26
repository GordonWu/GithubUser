package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.AsyncJunit
import gordon.lab.searchuser.customized.ui.userdetail.UserDetailState
import gordon.lab.searchuser.data.model.UserDetail
import gordon.lab.searchuser.data.repository.UserDetailRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDetailViewModelTest {

    @MockK(relaxed = true)
    lateinit var repo : UserDetailRepository
//    @MockK(relaxed = true)
//    lateinit var api: GithubApi


//    @MockK(relaxed = true)
    lateinit var viewModel: UserDetailViewModel

//    @get:Rule
//    var coroutineRule = MainCoroutineRule()
    var asyncJunit = AsyncJunit()

    private val job = Job()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(job + testDispatcher)

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    private val mockUserDetail = UserDetail(
        "thisIsAvatarUrl",
        "thisIsBIO",
        "thisIsBlog",
        "thisIsCompany",
        "email",
        9527,
        "taiwan",
        "GordonWu",
        "gordon"
    )

    @Test
    fun testFetchUserDetailStateData() = testScope.runTest {
//        api = GithubApiProvides()
        coEvery {  repo.getUserDetail("GordonWu")  }.returns( mockUserDetail )
        viewModel = UserDetailViewModel(repo, asyncJunit)
        viewModel.fetchUserDetail("GordonWu")
        assert (viewModel.viewState.value.userDetailState is UserDetailState.Fetched)
        (viewModel.viewState.value.userDetailState as UserDetailState.Fetched).run {
            assert(this.result.avatarUrl == "thisIsAvatarUrl")
            assert(this.result.bio == "thisIsBIO")
            assert(this.result.blog == "thisIsBlog")
            assert(this.result.company == "thisIsCompany")
            assert(this.result.email == "email")
            assert(this.result.location == "taiwan")
            assert(this.result.name == "gordon")
            assert(this.result.login == "GordonWu")
            assert(this.result.id == 9527)
        }
    }
}