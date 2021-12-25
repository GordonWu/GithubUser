package gordon.lab.searchuser.customized.protocol

sealed class MainEvent : uiEvent {
    object FetchUserList : MainEvent()
    data class FetchUserDetail(val userName:String = "") : MainEvent()
}