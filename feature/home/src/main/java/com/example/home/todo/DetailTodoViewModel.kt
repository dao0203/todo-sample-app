package com.example.home.todo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.todo.navigation.DETAIL_TODO_ID
import com.example.model.Todo
import com.example.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class DetailTodoViewModelState(
    val expandedDropDownMenu: Boolean = false,
    val showDeleteTodoDialog: Boolean = false,
)

@HiltViewModel
class DetailTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val todoId: Int = savedStateHandle.get<Int>(DETAIL_TODO_ID) ?: 0
    private val vmState = MutableStateFlow(DetailTodoViewModelState())

    val uiState = combine(
        vmState,
        todoRepository.observeById(todoId)
    ) { vmState, todo -> convertToUiState(todo, vmState) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            DetailTodoUiState.Loading()
        )

    private val _uiEvent = MutableSharedFlow<DetailTodoUiEvent>()

    val uiEvent = _uiEvent.asSharedFlow()

    fun showDropDownMenu() {
        vmState.update { it.copy(expandedDropDownMenu = true) }
    }

    fun dismissDropDownMenu() {
        vmState.update { it.copy(expandedDropDownMenu = false) }
    }

    fun completeTodo() {
        viewModelScope.launch { todoRepository.complete(todoId) }
    }

    fun undoCompleteTodo() {
        viewModelScope.launch { todoRepository.undoComplete(todoId) }
    }

    fun showDeleteTodoDialog() {
        vmState.update {
            it.copy(
                showDeleteTodoDialog = true,
                expandedDropDownMenu = false
            )
        }
    }

    fun dismissDeleteTodoDialog() {
        vmState.update { it.copy(showDeleteTodoDialog = false) }
    }

    fun deleteTodo() {
        viewModelScope.launch {
            vmState.update { it.copy(showDeleteTodoDialog = false) }
            todoRepository.delete((uiState.value as DetailTodoUiState.Success).todo)
            _uiEvent.emit(DetailTodoUiEvent.NavigateToBack)
        }
    }

    fun back() {
        viewModelScope.launch { _uiEvent.emit(DetailTodoUiEvent.NavigateToBack) }
    }

    private fun convertToUiState(todo: Todo, vmState: DetailTodoViewModelState): DetailTodoUiState {
        return DetailTodoUiState.Success(
            todo = todo,
            expandedDropDownMenu = vmState.expandedDropDownMenu,
            showDeleteTodoDialog = vmState.showDeleteTodoDialog,
        )
    }
}
