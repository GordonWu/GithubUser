package gordon.lab.searchuser.data

import com.google.gson.annotations.SerializedName

data class SearchUser(
    @SerializedName("total_count"        ) var totalCount        : Int        = 0,
    @SerializedName("incomplete_results" ) var incompleteResults : Boolean    = true,
    @SerializedName("items"              ) var UserItems            : ArrayList<UserItems> = arrayListOf()
)

data class UserItems(
    @SerializedName("id"                  ) var id                : Int     = 0,
    @SerializedName("login"               ) var login             : String  = "",
    @SerializedName("avatar_url"          ) var avatarUrl         : String  = "",
    @SerializedName("url"                 ) var url               : String  = ""
)