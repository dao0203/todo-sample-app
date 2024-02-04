package com.example.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM categories WHERE id = :categoryId")
    fun delete(categoryId: Int)
}
