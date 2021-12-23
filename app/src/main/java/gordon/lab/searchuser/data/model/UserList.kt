package gordon.lab.searchuser.data.model

import com.google.gson.annotations.SerializedName

data class UserList(
    @SerializedName("items"              ) var UserItems            : ArrayList<UserItems> = arrayListOf()
)

data class UserItems(
     var id                : Int     = 0,
     var login             : String  = "",
    @SerializedName("avatar_url"          ) var avatarUrl         : String  = "",
     var url               : String  = ""
)