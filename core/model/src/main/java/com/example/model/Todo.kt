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
    fun toDataString(): String {
        return """
            {
                "id": $id,
                "title": "$title",
                "description": "$description",
                "categoryId": $categoryId,
                "isCompleted": $isCompleted,
                "dueDate": $dueDate
            }
        """.trimIndent()
    }

    companion object {
        fun fromDataString(data: String): Todo {
            val json = JSONObject(data)
            return Todo(
                id = json.getString("id").toInt(),
                title = json.getString("title"),
                description = json.getString("description"),
                categoryId = json.getString("categoryId").toInt(),
                isCompleted = json.getBoolean("isCompleted"),
                dueDate = json.getLong("dueDate")
            )
        }
    }
}
