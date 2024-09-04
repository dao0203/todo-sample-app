package com.example.repository

import com.example.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTodoRepository : TodoRepository {
    override fun observeUncompletedByCategory(categoryId: Int): Flow<List<Todo>> {
        return flow {
            emit(listOf())
        }
    }

    override fun observeCompletedByCategory(categoryId: Int): Flow<List<Todo>> {
        return flow {
            emit(listOf())
        }
    }

    override fun observeById(todoId: Int): Flow<Todo> {
        return flow {
            emit(
                Todo(
                    1,
                    "title",
                    "description",
                    0,
                    false,
                    dueDate = 0,
                )
            )
        }
    }

    override suspend fun create(todo: Todo) {
        // no-op
    }

    override suspend fun delete(todo: Todo) {
        // no-op
    }

    override suspend fun complete(todoId: Int) {
        // no-op
    }

    override suspend fun undoComplete(todoId: Int) {
        // no-op
    }
}