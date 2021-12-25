package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.customized.protocol.MainEvent
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @MockK
    private lateinit var listRepo: UserListRepository
    private lateinit var viewModel: UserListViewModel
    var coroutineRule = CoroutineTestRule()
    private val job = Job()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testUserListState() = coroutineRule.testDispatcher.runBlockingTest {

        viewModel = UserListViewModel(listRepo)
        launch {
            viewModel.viewEvent.collect {
                print("====SHIT====")
            }
        }

            repeat(10) { _ ->
                print("====FUCK====")
                viewModel.setEvent(MainEvent.FetchUserList)
                delay(1000)
            }
            job.cancel()

    }
}