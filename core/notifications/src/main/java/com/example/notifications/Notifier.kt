package com.example.notifications

import com.example.model.Todo

interface Notifier {
    fun postExpiringTodoNotification(todo: Todo)
}
