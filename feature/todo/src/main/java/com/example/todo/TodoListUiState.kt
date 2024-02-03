package com.example.todo

import com.example.model.Category
import com.example.model.Todo

sealed interface TodoListUiState {
    val todos: List<Todo>
    val categories: List<Category>
    val isLoading: Boolean

    data class Loading(
        override val todos: List<Todo> = emptyList(),
        override val categories: List<Category> = emptyList(),
        override val isLoading: Boolean = true
    ) : TodoListUiState

    data class Success(
        override val todos: List<Todo>,
        override val categories: List<Category>,
        val selectedCategoryId: Int,
        override val isLoading: Boolean = false
    ) : TodoListUiState

    data class Error(
        val errorMessage: String,
        override val categories: List<Category> = emptyList(),
        override val todos: List<Todo> = emptyList(),
        override val isLoading: Boolean = false
    ) : TodoListUiState
}