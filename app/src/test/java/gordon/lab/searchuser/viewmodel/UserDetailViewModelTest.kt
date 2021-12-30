package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.AsyncJunit
import gordon.lab.searchuser.customized.ui.userdetail.UserDetailState
import gordon.lab.searchuser.data.model.UserDetail
import gordon.lab.searchuser.data.repository.UserDetailRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDetailViewModelTest {

    @MockK(relaxed = true)
    lateinit var repo : UserDetailRepository

     lateinit var viewModel: UserDetailViewModel

    private var asyncJunit = AsyncJunit()

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
    fun testFetchUserDetailStateData() {
//        api = GithubApiProvides()
        coEvery {  repo.getUserDetail("GordonWu")  }.returns( mockUserDetail )

        viewModel = UserDetailViewModel(repo, asyncJunit)
        assert (viewModel.viewState.value is UserDetailState.Idle)
        viewModel.fetchUserDetail("GordonWu")
        assert (viewModel.viewState.value is UserDetailState.Fetched)

        (viewModel.viewState.value as UserDetailState.Fetched).run {
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