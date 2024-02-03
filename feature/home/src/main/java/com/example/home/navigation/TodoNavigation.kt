package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.TodoListScreen

const val TODO_LIST_ROUTE = "todoList"

fun NavController.navigateToTodoList() {
    navigate(TODO_LIST_ROUTE)
}

fun NavGraphBuilder.todoListScreen() {
    composable(TODO_LIST_ROUTE) {
        TodoListScreen()
    }
}