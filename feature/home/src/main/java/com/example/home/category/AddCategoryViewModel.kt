package com.example.home.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Category
import com.example.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class AddCategoryViewModelState(
    val name: String = "",
    val enabledCompleteButton: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val vmState = MutableStateFlow(AddCategoryViewModelState())

    val uiState: StateFlow<AddCategoryUiState> = vmState.map {
        AddCategoryUiState(
            name = it.name,
            isLoading = it.isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddCategoryUiState()
    )

    private val _uiEvent = MutableSharedFlow<AddCategoryUiEvent>()

    val uiEvent: SharedFlow<AddCategoryUiEvent> = _uiEvent.asSharedFlow()

    fun changeName(name: String) {
        vmState.update {
            it.copy(
                name = name,
                enabledCompleteButton = name.isNotBlank()
            )
        }
    }

    fun addCategory() {
        viewModelScope.launch {
            vmState.update { it.copy(isLoading = true, enabledCompleteButton = false) }
            val category = Category(
                id = 0,
                name = vmState.value.name
            )
            categoryRepository.create(category)
            _uiEvent.emit(AddCategoryUiEvent.TransitionBack)
        }
    }

    fun back() {
        viewModelScope.launch {
            _uiEvent.emit(AddCategoryUiEvent.TransitionBack)
        }
    }
}
