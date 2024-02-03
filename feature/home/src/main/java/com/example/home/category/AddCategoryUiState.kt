package com.example.home.category

data class AddCategoryUiState(
    val name: String = "",
    val enabledCompleteButton: Boolean = false,
    val isLoading: Boolean = false,
)
