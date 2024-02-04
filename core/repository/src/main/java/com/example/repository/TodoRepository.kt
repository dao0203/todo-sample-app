package com.example.repository

import com.example.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeUncompletedByCategory(categoryId: Int): Flow<List<Todo>>

    suspend fun create(todo: Todo)
    suspend fun complete(todo: Todo)
    suspend fun undoComplete(todo: Todo)
}
