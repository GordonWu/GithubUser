package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.core.SearchUserAppModule
import gordon.lab.searchuser.customized.ui.userlist.UserListState
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.util.InstantExecutorExtension
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserListViewModelTest {

//    private val listRepo = mockkClass(UserListRepository::class)
     @MockK
    private lateinit var listRepo: UserListRepository

    @BeforeAll
    private fun doOnBeforeAll() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testUserListState() = coroutineRule.runBlockingTest{
        listRepo = UserListRepository(SearchUserAppModule.GithubApiProvides())
        val viewModel = UserListViewModel( repository = listRepo )
         viewModel.fetchUserList()
        if (viewModel.viewState.value.userListState is UserListState.Idle )
        {
            print("幹你娘")
        }
        if (viewModel.viewState.value.userListState is UserListState.Loading )
        {
            print("操你嗎")
        }
//        delay(3000)

        if (viewModel.viewState.value.userListState is UserListState.Fetched )
        {
            print("塞林揚")
        }
        if (viewModel.viewState.value.userListState is UserListState.Error )
        {
            print("吃屎吧")
        }
    }
}