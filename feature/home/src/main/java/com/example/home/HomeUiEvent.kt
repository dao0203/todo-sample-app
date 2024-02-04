package com.example.home

sealed interface HomeUiEvent {
    data object NavigateToAddCategory : HomeUiEvent
    data object NavigateToAddTodo : HomeUiEvent
    data class NavigateToDetail(val todoId: Int) : HomeUiEvent

    data class ShowSnackbar(val message: String) : HomeUiEvent
}
