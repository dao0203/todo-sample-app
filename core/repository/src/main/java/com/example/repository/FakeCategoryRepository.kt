package com.example.repository

import com.example.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCategoryRepository : CategoryRepository {
    private val categories = mutableListOf<Category>()

    override fun getAll(): Flow<List<Category>> {
        return flow {
            emit(categories)
        }
    }

    override suspend fun create(category: Category) {
        categories.add(category)
    }

    override suspend fun delete(categoryId: Int) {
        categories.removeIf { it.id == categoryId }
    }
}