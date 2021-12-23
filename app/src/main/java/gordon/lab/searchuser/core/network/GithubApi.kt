package gordon.lab.searchuser.core.network

import gordon.lab.searchuser.data.model.UserDetail
import gordon.lab.searchuser.data.model.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {


    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    suspend fun getUserList(@Query("since") since:Int): UserList

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users/{username}")
    suspend fun getUserDetail(@Path("username") username :String): UserDetail
}