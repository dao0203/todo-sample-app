package com.example.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEntity: TodoEntity)

    @Query("SELECT * FROM todos")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE category_id = :categoryId")
    fun getAllByCategory(categoryId: Int): Flow<List<TodoEntity>>
}