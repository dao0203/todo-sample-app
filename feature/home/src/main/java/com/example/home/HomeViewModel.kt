package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Category
import com.example.model.Todo
import com.example.repository.CategoryRepository
import com.example.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class HomeViewModelState(
    val selectedCategoryId: Int = 1,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val vmState = MutableStateFlow(HomeViewModelState())

    val uiState: StateFlow<HomeUiState> = vmState.flatMapLatest { vmState ->
        combine(
            categoryRepository.getAll(),
            todoRepository.observeUnCompletedByCategory(vmState.selectedCategoryId)
        ) { category, todos ->
            convertToUiState(category, todos, vmState)
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeUiState.Loading()
        )

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()

    val uiEvent = _uiEvent.asSharedFlow()

    fun complete(todo: Todo) {
        viewModelScope.launch {
            todoRepository.complete(todo)
            _uiEvent.emit(HomeUiEvent.CompletionChanged(todo))
        }
    }

    fun undoComplete(todo: Todo) {
        viewModelScope.launch {
            todoRepository.undoComplete(todo)
        }
    }

    fun changeCategory(categoryId: Int) {
        vmState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun navigateToAddCategory() {
        viewModelScope.launch { _uiEvent.emit(HomeUiEvent.NavigateToAddCategory) }
    }

    fun navigateToAddTodo() {
        viewModelScope.launch { _uiEvent.emit(HomeUiEvent.NavigateToAddTodo) }
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
