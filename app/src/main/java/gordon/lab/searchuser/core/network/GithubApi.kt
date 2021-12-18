package gordon.lab.searchuser.core.network

import gordon.lab.searchuser.data.model.SearchUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApi {


    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/search/users")
    suspend fun getUserList(@Query("q") q:String,
                    @Query("sort") sort:String,
                    @Query("order") order:String,
                    @Query("pre_page") per_page:Int,
                    @Query("page") page:Int): SearchUser
}