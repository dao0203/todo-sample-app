package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Category
import com.example.model.Todo
import com.example.repository.CategoryRepository
import com.example.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private data class HomeViewModelState(
    val selectedCategoryId: Int = 1,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val vmState = MutableStateFlow(HomeViewModelState())

    val uiState: StateFlow<HomeUiState> = combine(
        categoryRepository.getAll(),
        todoRepository.getByCategory(vmState.value.selectedCategoryId),
        vmState
    ) { category, todos, vmState ->
        convertToUiState(category, todos, vmState)
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeUiState.Loading()
        )

    fun changeCategory(categoryId: Int) {
        vmState.value = vmState.value.copy(selectedCategoryId = categoryId)
    }

    private fun convertToUiState(
        category: List<Category>,
        todos: List<Todo>,
        vmState: HomeViewModelState
    ) = when {
        category.isEmpty() -> HomeUiState.Error("No categories found")
        else -> HomeUiState.Success(
            todos = todos,
            categories = category,
            selectedCategoryId = vmState.selectedCategoryId
        )
    }
}
