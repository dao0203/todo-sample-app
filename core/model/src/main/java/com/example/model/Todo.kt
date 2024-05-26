package com.example.model

import org.json.JSONObject

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val categoryId: Int,
    val isCompleted: Boolean,
    val dueDate: Long
) {
    fun toJson(): String {
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("description", description)
            put("category_id", categoryId)
            put("is_completed", isCompleted)
            put("due_date", dueDate)
        }.toString()
    }

    companion object {
        fun fromJson(data: String): Todo {
            val json = JSONObject(data)
            return Todo(
                id = json.getInt("id"),
                title = json.getString("title"),
                description = json.getString("description"),
                categoryId = json.getInt("category_id"),
                isCompleted = json.getBoolean("is_completed"),
                dueDate = json.getLong("due_date")
            )
        }
    }
}
