package com.example.home.todo

sealed interface DetailTodoUiEvent {
    data object NavigateToBack : DetailTodoUiEvent
}
