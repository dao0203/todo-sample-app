package com.example.home.todo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.todo.AddTodoScreen

internal const val ADD_TODO_ROUTE = "addTodo"

internal fun NavController.navigateToAddTodo() {
    navigate(ADD_TODO_ROUTE)
}

internal fun NavGraphBuilder.addTodoScreen(
    popBackStack: () -> Unit
) {
    composable(ADD_TODO_ROUTE) {
        AddTodoScreen(onClickBack = popBackStack)
    }
}