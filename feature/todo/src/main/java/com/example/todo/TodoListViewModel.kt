package com.example.todo

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

private data class TodoListViewModelState(
    val selectedCategoryId: Int = 1,
)

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val vmState = MutableStateFlow(TodoListViewModelState())

    val uiState: StateFlow<TodoListUiState> = combine(
        categoryRepository.getAll(),
        todoRepository.getByCategory(vmState.value.selectedCategoryId),
        vmState
    ) { category, todos, vmState ->
        convertToUiState(category, todos, vmState)
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TodoListUiState.Loading()
        )

    fun changeCategory(categoryId: Int) {
        vmState.value = vmState.value.copy(selectedCategoryId = categoryId)
    }

    private fun convertToUiState(
        category: List<Category>,
        todos: List<Todo>,
        vmState: TodoListViewModelState
    ) = when {
        category.isEmpty() -> TodoListUiState.Error("No categories found")
        else -> TodoListUiState.Success(
            todos = todos,
            categories = category,
            selectedCategoryId = vmState.selectedCategoryId
        )
    }
}
