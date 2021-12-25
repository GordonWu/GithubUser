package gordon.lab.searchuser.core

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gordon.lab.searchuser.Constants.baseURL
import gordon.lab.searchuser.core.network.GithubApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object SearchUserAppModule {

    @Provides
    fun GithubApiProvides() :GithubApi{
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request: Request = chain.request()
                    val response = chain.proceed(request)

                     when(response.code){
                        422->{
                            Log.d("API", "Unprocessable Entity")
                        }
                        503->{
                            Log.d("API", "Service Unavailable")
                        }
                     }

                    response
                })
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi::class.java)
    }

}