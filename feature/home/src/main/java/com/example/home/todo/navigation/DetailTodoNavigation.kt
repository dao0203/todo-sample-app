package com.example.home.todo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.home.todo.DetailTodoScreen

internal const val DETAIL_TODO_ROUTE = "detailTodo"

internal const val DETAIL_TODO_ID = "detailTodoId"

internal fun NavController.navigateToDetailTodo(todoId: Int) {
    navigate("$DETAIL_TODO_ROUTE/$todoId")
}

internal fun NavGraphBuilder.detailTodoScreen(
    popBackStack: () -> Unit
) {
    composable(
        "$DETAIL_TODO_ROUTE/{$DETAIL_TODO_ID}",
        arguments = listOf(navArgument(DETAIL_TODO_ID) { type = NavType.IntType })
    ) {
        DetailTodoScreen(onClickBack = popBackStack)
    }
}
