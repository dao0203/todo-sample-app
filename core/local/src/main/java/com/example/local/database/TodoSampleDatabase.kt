package com.example.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.local.dao.CategoryEntityDao
import com.example.local.dao.TodoEntityDao
import com.example.local.entity.CategoryEntity
import com.example.local.entity.TodoEntity

@Database(entities = [TodoEntity::class, CategoryEntity::class], version = 1)
abstract class TodoSampleDatabase: RoomDatabase() {
    abstract fun todoEntityDao(): TodoEntityDao
    abstract fun categoryEntityDao(): CategoryEntityDao
}
