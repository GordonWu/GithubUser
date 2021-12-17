package gordon.lab.searchuser.core.network

import android.util.Log
import gordon.lab.searchuser.data.SearchUser
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApi {

    companion object{
        private const val baseURL="https://api.github.com"

        fun create(): GithubApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi::class.java)
        }
    }

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/search/users")
    suspend fun getUserList(@Query("q") q:String,
                    @Query("sort") sort:String,
                    @Query("order") order:String,
                    @Query("pre_page") per_page:Int,
                    @Query("page") page:Int): SearchUser
}