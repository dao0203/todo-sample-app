package com.example.local.entity

import androidx.room.Entity

@Entity(tableName = "categories")
data class CategoryEntity(
    val id: Int,
    val name: String,
)
