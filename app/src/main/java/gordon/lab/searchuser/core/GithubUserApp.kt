package gordon.lab.searchuser.core

import android.app.Application
import gordon.lab.searchuser.core.network.GithubApi
import gordon.lab.searchuser.core.network.provideLoggingInterceptor
import gordon.lab.searchuser.core.network.provideOkHttpClient
import gordon.lab.searchuser.core.network.provideRetrofit
import gordon.lab.searchuser.customized.protocol.AsyncDelegate
import gordon.lab.searchuser.data.repository.UserDetailRepository
import gordon.lab.searchuser.data.repository.UserListRepository
import gordon.lab.searchuser.viewmodel.UserDetailViewModel
import gordon.lab.searchuser.viewmodel.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GithubUserApp: Application(){
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            modules(
                module {
                    factory { provideOkHttpClient(get()) }
                    factory { GithubApi(get()) }
                    factory { provideLoggingInterceptor() }
                    single { provideRetrofit(get()) }


                    factory { UserDetailRepository(get()) }
                    factory { UserListRepository(get()) }

                    single<AsyncDelegate> { GithubUserAsyncApp() }
                    viewModel { UserListViewModel(get(),get()) }
                    viewModel { UserDetailViewModel(get(),get()) }
                }
            )
        }
    }
}