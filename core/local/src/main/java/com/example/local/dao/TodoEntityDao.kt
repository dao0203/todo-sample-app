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

    @Query("DELETE FROM todos WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM todos")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE category_id = :categoryId")
    fun getAllByCategory(categoryId: Int): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE category_id = :categoryId AND is_completed = false")
    fun getUnCompletedByCategory(categoryId: Int): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE category_id = :categoryId AND is_completed = true")
    fun getCompletedByCategory(categoryId: Int): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    fun getById(id: Int): Flow<TodoEntity?>

    @Query("UPDATE todos SET is_completed = true WHERE id = :id")
    fun complete(id: Int)

    @Query("UPDATE todos SET is_completed = false WHERE id = :id")
    fun undoComplete(id: Int)
}