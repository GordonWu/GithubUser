package gordon.lab.searchuser.viewmodel

import androidx.lifecycle.ViewModel
import gordon.lab.searchuser.customized.protocol.AsyncDelegate
import gordon.lab.searchuser.customized.protocol.uiEvent
import gordon.lab.searchuser.customized.protocol.uiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<Event:uiEvent,State:uiState>(private val asyncDelegate : AsyncDelegate) :ViewModel(){

//    var ui: CoroutineDispatcher = Dispatchers.Main
//    var io: CoroutineDispatcher =  Dispatchers.IO
//    var background: CoroutineDispatcher = Dispatchers.Default

    // Create Initial State of View
    private val initialState : State by lazy { onInitState() }
    abstract fun onInitState() : State
    abstract fun onHandleEvent(event : Event)

    // Get Current State
    private val currentState: State
        get() = viewState.value

    private val _uiState : MutableStateFlow<State> = MutableStateFlow(initialState)
    val viewState = _uiState.asStateFlow()

    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val viewEvent = _event.asSharedFlow()

    init {
        subscribeEvents()
    }

//    fun ViewModel.uiJob(block: suspend CoroutineScope.() -> Unit): Job {
//        return viewModelScope.launch(ui) {
//            block()
//        }
//    }
//
//    fun ViewModel.ioJob(block: suspend CoroutineScope.() -> Unit): Job {
//        return viewModelScope.launch(io) {
//            block()
//        }
//    }
//
//    fun ViewModel.backgroundJob(block: suspend CoroutineScope.() -> Unit): Job {
//        return viewModelScope.launch(background) {
//            block()
//        }
//    }

    fun setEvent(event : Event) {
        val newEvent = event
        asyncDelegate.ioJob { _event.emit(newEvent) }
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     * Start listening to Event
     */
    private fun subscribeEvents() {
        asyncDelegate.ioJob {
           viewEvent.collect {
               onHandleEvent(it)
           }
        }
    }
}