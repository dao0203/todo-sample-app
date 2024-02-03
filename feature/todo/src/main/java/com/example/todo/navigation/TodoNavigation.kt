package com.example.todo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todo.TodoListScreen

const val TODO_LIST_ROUTE = "todoList"

fun NavController.navigateToTodoList() {
    navigate(TODO_LIST_ROUTE)
}

fun NavGraphBuilder.todoListScreen() {
    composable(TODO_LIST_ROUTE) {
        TodoListScreen()
    }
}