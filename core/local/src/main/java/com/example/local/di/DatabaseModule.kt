package com.example.local.di

import android.content.Context
import androidx.room.Room
import com.example.local.database.TodoSampleDatabase
import com.example.local.database.TodoSampleDatabaseCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Volatile
    private var INSTANCE: TodoSampleDatabase? = null

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoSampleDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                TodoSampleDatabase::class.java,
                "todo_sample_database"
            )
                .addCallback(TodoSampleDatabaseCallback())
                .build()
            INSTANCE = instance
            instance
        }
    }

    @Provides
    @Singleton
    fun provideTodoEntityDao(database: TodoSampleDatabase) = database.todoEntityDao()

    @Provides
    @Singleton
    fun provideCategoryEntityDao(database: TodoSampleDatabase) = database.categoryEntityDao()
}
