package gordon.lab.searchuser.util

sealed class MainIntent {
    data class FetchUserList(val userID:Int =0 ) : MainIntent()
    data class FetchUserDetail(val userName:String = "") : MainIntent()
}