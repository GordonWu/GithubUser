package gordon.lab.searchuser.viewmodel

import androidx.lifecycle.ViewModel
import gordon.lab.searchuser.core.AsyncTaskDelegate
import gordon.lab.searchuser.customized.protocol.uiEvent
import gordon.lab.searchuser.customized.protocol.uiState
import kotlinx.coroutines.flow.*

abstract class BaseViewModel<Event:uiEvent,State:uiState>(private val asyncTaskDelegate: AsyncTaskDelegate) :ViewModel(){

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

    fun setEvent(event : Event) {
        val newEvent = event
        asyncTaskDelegate.ioJob { _event.emit(newEvent) }
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     * Start listening to Event
     */
    private fun subscribeEvents() {
        asyncTaskDelegate.ioJob {
           viewEvent.collect {
               onHandleEvent(it)
           }
        }
    }
}