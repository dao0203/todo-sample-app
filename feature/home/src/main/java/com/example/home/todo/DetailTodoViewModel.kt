package com.example.home.todo

import androidx.lifecycle.ViewModel
import com.example.model.Todo
import com.example.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel

private data class DetailTodoViewModelState(
    val todo: Todo? = null
)

@HiltViewModel
class DetailTodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

}