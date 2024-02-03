package com.example.home.todo

sealed interface AddTodoUiEvent {
    data object TransitionBack : AddTodoUiEvent
}
