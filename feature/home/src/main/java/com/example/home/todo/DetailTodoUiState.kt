package com.example.home.todo

import com.example.model.Todo

sealed interface DetailTodoUiState {
    val expandedDropDownMenu: Boolean
    val showDeleteTodoDialog: Boolean

    data class Loading(
        override val expandedDropDownMenu: Boolean = false,
        override val showDeleteTodoDialog: Boolean = false,
    ) : DetailTodoUiState

    data class Success(
        val todo: Todo,
        override val expandedDropDownMenu: Boolean = false,
        override val showDeleteTodoDialog: Boolean = false,
    ) : DetailTodoUiState
}
