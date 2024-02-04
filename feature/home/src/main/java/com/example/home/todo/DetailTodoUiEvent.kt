package com.example.home.todo

sealed interface DetailTodoUiEvent {
    data object NavigateToBack : DetailTodoUiEvent
    data class ShowSnackBar(val message: String) : DetailTodoUiEvent
}
