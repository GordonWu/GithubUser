package gordon.lab.searchuser.util

sealed class MainIntent {
    object FetchUserList : MainIntent()
    data class FetchUserDetail(val userName:String = "") : MainIntent()
}