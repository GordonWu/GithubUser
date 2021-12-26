package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.AsyncJunit
import gordon.lab.searchuser.customized.ui.userlist.UserListState
import gordon.lab.searchuser.data.model.ApiResult
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.data.repository.UserListRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class UserListViewModelTest{

    @MockK(relaxed = true)
    lateinit var repo : UserListRepository

    lateinit var viewModel: UserListViewModel
    private var asyncJunit = AsyncJunit()

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    private fun mockUserList() : ApiResult{
        val userList = arrayListOf<UserItems>()
        (0 until 30).forEach {
            userList.add(UserItems(
                avatarURL = "url${it}",
                id = it,
                login = "LoginName${it}",
            ))
        }

       return ApiResult(userList)
    }

    @Test
    fun testFetchUserListStateData() {
        coEvery {  repo.getUserList()  }.returns( mockUserList() )

        viewModel = UserListViewModel(repo, asyncJunit)
        assert (viewModel.viewState.value.userListState is UserListState.Idle)
        viewModel.fetchUserList()
        assert (viewModel.viewState.value.userListState is UserListState.Fetched)

        (viewModel.viewState.value.userListState as UserListState.Fetched).run {
            assert(this.result.userList.size == 30)
            assert(this.result.userList[15].id == 15)
            assert(this.result.userList.last().id == 29)
        }
    }
}