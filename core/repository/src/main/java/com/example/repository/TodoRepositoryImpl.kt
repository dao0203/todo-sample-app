package com.example.repository

import com.example.local.dao.TodoEntityDao
import com.example.local.entity.TodoEntity
import com.example.local.entity.toTodos
import com.example.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoEntityDao: TodoEntityDao
) : TodoRepository {
    override fun observeUncompletedByCategory(categoryId: Int): Flow<List<Todo>> =
        todoEntityDao.getUnCompletedByCategory(categoryId).map { it.toTodos() }

    override fun observeCompletedByCategory(categoryId: Int): Flow<List<Todo>> =
        todoEntityDao.getCompletedByCategory(categoryId).map { it.toTodos() }

    override fun observeById(todoId: Int): Flow<Todo> =
        todoEntityDao.getById(todoId).map { it.toTodo() }

    override suspend fun create(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoEntityDao.insert(TodoEntity.fromTodo(todo))
        }
    }

    override suspend fun delete(todoId: Int) {
        withContext(Dispatchers.IO) {
            todoEntityDao.delete(todoId)
        }
    }

    override suspend fun complete(todoId: Int) {
        withContext(Dispatchers.IO) {
            todoEntityDao.complete(todoId)
        }
    }

    override suspend fun undoComplete(todoId: Int) {
        withContext(Dispatchers.IO) {
            todoEntityDao.undoComplete(todoId)
        }
    }
}
