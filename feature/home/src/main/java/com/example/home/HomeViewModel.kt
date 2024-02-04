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
    val showConfirmDeleteDialog: Boolean = false,
    val showCompletedTodos: Boolean = false,
    val expandedDropDownMenu: Boolean = false,
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
            todoRepository.observeUncompletedByCategory(vmState.selectedCategoryId),
            todoRepository.observeCompletedByCategory(vmState.selectedCategoryId)
        ) { category, uncompletedTodos, completedTodos ->
            convertToUiState(
                category,
                uncompletedTodos,
                completedTodos,
                vmState
            )
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeUiState.Loading()
        )

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()

    val uiEvent = _uiEvent.asSharedFlow()

    //todo
    fun complete(todo: Todo) {
        viewModelScope.launch {
            todoRepository.complete(todo.id)
            _uiEvent.emit(HomeUiEvent.CompletionChanged(todo))
        }
    }

    fun undoComplete(todo: Todo) {
        viewModelScope.launch {
            todoRepository.undoComplete(todo.id)
        }
    }

    fun toggleShowCompletedTodos() {
        vmState.update { it.copy(showCompletedTodos = !it.showCompletedTodos) }
    }

    //dialog
    fun showConfirmDeleteDialog() {
        if (vmState.value.selectedCategoryId == 1) return
        vmState.update {
            it.copy(
                showConfirmDeleteDialog = true,
                expandedDropDownMenu = false
            )
        }
    }

    fun dismissConfirmDeleteDialog() {
        vmState.update { it.copy(showConfirmDeleteDialog = false) }
    }

    //dropdown
    fun expandDropDownMenu() {
        vmState.update { it.copy(expandedDropDownMenu = true) }
    }

    fun dismissDropDownMenu() {
        vmState.update { it.copy(expandedDropDownMenu = false) }
    }

    //category
    fun changeCategory(categoryId: Int) {
        vmState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            val id = vmState.value.selectedCategoryId
            vmState.update { it.copy(selectedCategoryId = 1) }
            categoryRepository.delete(id)
            vmState.update { it.copy(showConfirmDeleteDialog = false) }
        }
    }

    //navigation
    fun navigateToAddCategory() {
        viewModelScope.launch { _uiEvent.emit(HomeUiEvent.NavigateToAddCategory) }
    }

    fun navigateToAddTodo() {
        viewModelScope.launch { _uiEvent.emit(HomeUiEvent.NavigateToAddTodo) }
    }

    fun navigateToDetail(todo: Todo) {
        viewModelScope.launch { _uiEvent.emit(HomeUiEvent.NavigateToDetail(todo.id)) }
    }

    private fun convertToUiState(
        category: List<Category>,
        uncompletedTodos: List<Todo>,
        completedTodos: List<Todo>,
        vmState: HomeViewModelState
    ) = when {
        category.isEmpty() -> HomeUiState.Error(
            "No categories found",
            showConfirmDeleteDialog = vmState.showConfirmDeleteDialog,
            expandedDropDownMenu = vmState.expandedDropDownMenu
        )

        else -> HomeUiState.Success(
            uncompletedTodos = uncompletedTodos,
            completedTodos = completedTodos,
            categories = category,
            selectedCategoryId = vmState.selectedCategoryId,
            showConfirmDeleteDialog = vmState.showConfirmDeleteDialog,
            showCompletedTodos = vmState.showCompletedTodos,
            expandedDropDownMenu = vmState.expandedDropDownMenu
        )
    }
}
