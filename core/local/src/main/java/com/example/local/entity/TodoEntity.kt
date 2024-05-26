package com.example.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.model.Todo

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    @ColumnInfo(name = "due_date")
    val dueDate: Long
) {
    fun toTodo() = Todo(
        id = id,
        title = title,
        description = description,
        categoryId = categoryId,
        isCompleted = isCompleted,
        dueDate = dueDate
    )

    companion object {
        fun fromTodo(todo: Todo) = TodoEntity(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            categoryId = todo.categoryId,
            isCompleted = todo.isCompleted,
            dueDate = todo.dueDate
        )
    }
}

fun List<TodoEntity>.toTodos() = map(TodoEntity::toTodo)