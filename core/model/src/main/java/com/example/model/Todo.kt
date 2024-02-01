package com.example.model

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val categoryId: Int,
    val isCompleted: Boolean,
)
