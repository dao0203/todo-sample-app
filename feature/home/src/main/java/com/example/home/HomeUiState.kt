package com.example.home

import com.example.model.Category
import com.example.model.Todo

sealed interface HomeUiState {
    val uncompletedTodos: List<Todo>
    val completedTodos: List<Todo>
    val categories: List<Category>
    val isLoading: Boolean
    val showConfirmDeleteDialog: Boolean
    val showCompletedTodos: Boolean
    val expandedDropDownMenu: Boolean

    data class Loading(
        override val uncompletedTodos: List<Todo> = emptyList(),
        override val completedTodos: List<Todo> = emptyList(),
        override val categories: List<Category> = emptyList(),
        override val isLoading: Boolean = true,
        override val showConfirmDeleteDialog: Boolean = false,
        override val showCompletedTodos: Boolean = false,
        override val expandedDropDownMenu: Boolean = false
    ) : HomeUiState

    data class Success(
        override val uncompletedTodos: List<Todo>,
        override val completedTodos: List<Todo>,
        override val categories: List<Category>,
        val selectedCategoryId: Int,
        override val isLoading: Boolean = false,
        override val showConfirmDeleteDialog: Boolean = false,
        override val showCompletedTodos: Boolean = false,
        override val expandedDropDownMenu: Boolean = false
    ) : HomeUiState

    data class Error(
        val errorMessage: String,
        override val categories: List<Category> = emptyList(),
        override val uncompletedTodos: List<Todo> = emptyList(),
        override val completedTodos: List<Todo> = emptyList(),
        override val isLoading: Boolean = false,
        override val showConfirmDeleteDialog: Boolean = false,
        override val showCompletedTodos: Boolean = false,
        override val expandedDropDownMenu: Boolean = false
    ) : HomeUiState
}
