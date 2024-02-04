package com.example.repository

import com.example.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<Category>>
    suspend fun create(category: Category)
    suspend fun delete(categoryId: Int)
}
