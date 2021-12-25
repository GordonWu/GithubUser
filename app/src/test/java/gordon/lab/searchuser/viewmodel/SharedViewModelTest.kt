package gordon.lab.searchuser.viewmodel

import gordon.lab.searchuser.core.SearchUserAppModule.GithubApiProvides
import gordon.lab.searchuser.data.model.UserItems
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.util.MainCoroutineRule
import gordon.lab.searchuser.util.MainIntent
import gordon.lab.searchuser.util.MainState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun testState() = coroutineRule.runBlockingTest {
        repo = UserListRepository(GithubApiProvides())

//        every { repo.userListCache } returns emptyList()
        sharedViewModel = SharedViewModel(repo)

        // idle, loading, fetch, error, error true meaning we don't expect get error state for this time
        val actual = booleanArrayOf(false,false,false,true)

        val expect = booleanArrayOf(true, true, true , true)

        val job = launch {
            sharedViewModel.userIntent.send(MainIntent.FetchUserList)
            sharedViewModel.state.collect() {
                when (it) {
                    is MainState.Idle -> {
                        print("A======")
                        actual[0] = true
                    }
                    is MainState.Loading -> {
                        print("B======")
                        actual[1] = true
                    }
                    is MainState.DataFetched -> {
                        print("D======")
                        actual[2] = true
                    }
                    is MainState.Error -> {
                        print("E======")
                        actual[3] = false
                    }
                }
            }
            print("F======")

            Assert.assertArrayEquals(expect, actual);
        }

        job.cancel()
    }

     private fun mockUserList(size: Int): List<UserItems> {
         return (0 until size).map {
             UserItems(id = it , login = "login$it", avatarURL =  "avatar$it")
         }
     }

 }