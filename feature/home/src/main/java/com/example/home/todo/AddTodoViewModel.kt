package com.example.home.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Category
import com.example.model.Todo
import com.example.repository.CategoryRepository
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

private data class AddTodoViewModelState(
    val title: String = "",
    val description: String = "",
    val selectedCategory: Category? = null,
    val categoryDropDownExpanded: Boolean = false,
    val enabledCompleteButton: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {
    private val vmState = MutableStateFlow(AddTodoViewModelState())

    private val _uiEvent = MutableSharedFlow<AddTodoUiEvent>()

    val uiEvent = _uiEvent.asSharedFlow()

    val uiState = combine(
        categoryRepository.getAll(),
        vmState
    ) { categories, vmState -> convertToUiState(categories, vmState) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            AddTodoUiState()
        )

    fun changeTitle(title: String) {
        vmState.update {
            it.copy(
                title = title,
                enabledCompleteButton = title.isNotBlank() && it.selectedCategory != null
            )
        }
    }

    fun changeDescription(description: String) {
        vmState.update {
            it.copy(
                description = description,
                enabledCompleteButton = description.isNotBlank() && it.selectedCategory != null
            )
        }
    }

    fun changeCategory(category: Category) {
        vmState.update {
            it.copy(
                selectedCategory = category,
                categoryDropDownExpanded = false,
                enabledCompleteButton = it.title.isNotBlank()
            )
        }
    }

    fun addTodo() {
        if (vmState.value.selectedCategory == null) return
        viewModelScope.launch {
            vmState.update { it.copy(isLoading = true, enabledCompleteButton = false) }
            val todo = Todo(
                id = 0,
                title = vmState.value.title,
                description = vmState.value.description,
                categoryId = vmState.value.selectedCategory!!.id,
                isCompleted = false,
                dueDate = System.currentTimeMillis() + 300 * 1000
            )
            todoRepository.create(todo)
            vmState.update { it.copy(isLoading = false, enabledCompleteButton = true) }
            _uiEvent.emit(AddTodoUiEvent.NavigateToBack)
        }
    }

    fun back() {
        viewModelScope.launch {
            _uiEvent.emit(AddTodoUiEvent.NavigateToBack)
        }
    }

    fun toggleCategoryDropDown() {
        vmState.update {
            it.copy(categoryDropDownExpanded = !it.categoryDropDownExpanded)
        }
    }

    private fun convertToUiState(categories: List<Category>, vmState: AddTodoViewModelState) =
        AddTodoUiState(
            title = vmState.title,
            description = vmState.description,
            selectedCategory = vmState.selectedCategory,
            categories = categories,
            categoryDropDownExpanded = vmState.categoryDropDownExpanded,
            enabledCompleteButton = vmState.enabledCompleteButton,
            isLoading = vmState.isLoading
        )
}
