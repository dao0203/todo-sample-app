package com.example.repository

import com.example.local.dao.CategoryEntityDao
import com.example.local.entity.CategoryEntity
import com.example.local.entity.toCategories
import com.example.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao
) : CategoryRepository {
    override fun getAll(): Flow<List<Category>> =
        categoryEntityDao.getCategories()
            .map { it.toCategories() }

    override suspend fun create(category: Category) {
        withContext(Dispatchers.IO) {
            categoryEntityDao.insert(CategoryEntity.fromCategory(category))
        }
    }

    override suspend fun delete(categoryId: Int) {
        withContext(Dispatchers.IO) {
            categoryEntityDao.delete(categoryId)
        }
    }
}
