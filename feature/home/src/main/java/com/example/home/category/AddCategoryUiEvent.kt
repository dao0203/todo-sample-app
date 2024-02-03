package com.example.home.category

sealed interface AddCategoryUiEvent {
    data object TransitionBack : AddCategoryUiEvent
}
