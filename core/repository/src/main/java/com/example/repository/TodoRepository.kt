package com.example.repository

import com.example.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeUncompletedByCategory(categoryId: Int): Flow<List<Todo>>
    fun observeCompletedByCategory(categoryId: Int): Flow<List<Todo>>

    fun observeById(todoId: Int): Flow<Todo>
    suspend fun create(todo: Todo)
    suspend fun delete(todoId: Int)
    suspend fun complete(todoId: Int)
    suspend fun undoComplete(todoId: Int)
}
