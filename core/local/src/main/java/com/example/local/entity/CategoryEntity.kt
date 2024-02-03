package com.example.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
) {
    fun toCategory() = Category(
        id = id,
        name = name
    )

    companion object {
        fun fromCategory(category: Category) = CategoryEntity(
            id = category.id,
            name = category.name
        )
    }
}

fun List<CategoryEntity>.toCategories() = map(CategoryEntity::toCategory)
