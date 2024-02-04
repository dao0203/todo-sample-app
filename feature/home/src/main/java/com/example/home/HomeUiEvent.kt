package com.example.home

import com.example.model.Todo

sealed interface HomeUiEvent {
    data object NavigateToAddCategory : HomeUiEvent
    data object NavigateToAddTodo : HomeUiEvent
    data class NavigateToDetail(val todoId: Int) : HomeUiEvent
    data class CompletionChanged(val todo: Todo) : HomeUiEvent

}
