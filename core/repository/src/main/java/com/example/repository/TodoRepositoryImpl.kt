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
    override fun getByCategory(categoryId: Int): Flow<List<Todo>> =
        todoEntityDao.getAllByCategory(categoryId).map { it.toTodos() }

    override suspend fun create(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoEntityDao.insert(TodoEntity.fromTodo(todo))
        }
    }

    override suspend fun complete(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoEntityDao.complete(todo.id)
        }
    }

    override suspend fun undoComplete(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoEntityDao.undoComplete(todo.id)
        }
    }
}
