package gordon.lab.searchuser.viewmodel

import android.widget.Toast
import androidx.core.view.isVisible
import gordon.lab.searchuser.core.SearchUserAppModule.GithubApiProvides
import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.data.model.UserList
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.util.MainCoroutineRule
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

 class SharedViewModelTest {


    @MockK
    private lateinit var repo: UserListRepository
    private lateinit var sharedViewModel: SharedViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testState() = coroutineRule.runBlockingTest {
        repo = UserListRepository(GithubApiProvides())

        every { repo.userListCache } returns emptyList()
        sharedViewModel = SharedViewModel(repo)

        // idle, loading, fetch, error, error true meaning we don't expect get error state for this time
        val testPassed = booleanArrayOf(false,false,false,true)
        val expect = booleanArrayOf(true, true, true , true)
        sharedViewModel.userIntent.send(MainIntent.FetchUserList)
        sharedViewModel.userIntent.close()
        sharedViewModel.state.collect {
            when (it) {
                is MainState.Idle -> {
                    testPassed[0] = true
                }
                is MainState.Loading -> {
                    testPassed[1] = true
                }
                is MainState.DataFetched -> {
                    testPassed[2] = true
                }
                is MainState.Error -> {
                    testPassed[3] = false
                }
            }
        }

        Assert.assertArrayEquals(expect, testPassed);
    }

     private fun mockUserList(size: Int): List<UserItems> {
         return (0 until size).map {
             UserItems(id = it , login = "login$it", avatarURL =  "avatar$it")
         }
     }

 }