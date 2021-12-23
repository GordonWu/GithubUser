package gordon.lab.searchuser.data.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("avatar_url") var avatarUrl : String  = "",
    val bio: String,
    val blog: String,
    val company: String,
    val email: String,
    val id: Int,
    val location: String,
    val login: String,
    val name: String,
)