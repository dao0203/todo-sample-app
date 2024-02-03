package com.example.home.todo

import com.example.model.Category

data class AddTodoUiState(
    val title: String = "",
    val description: String = "",
    val selectedCategory: Category? = null,
    val categories: List<Category> = emptyList(),
    val categoryDropDownExpanded: Boolean = false,
    val enabledCompleteButton: Boolean = false,
    val isLoading: Boolean = false,
)
