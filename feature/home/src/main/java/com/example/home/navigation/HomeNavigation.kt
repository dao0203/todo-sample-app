package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.home.HomeScreen
import com.example.home.category.navigation.addCategoryScreen
import com.example.home.category.navigation.navigateToAddCategory
import com.example.home.todo.navigation.addTodoScreen
import com.example.home.todo.navigation.navigateToAddTodo

const val HOME_GRAPH_ROUTE = "homeGraph"

internal const val HOME_ROUTE = "home"


fun NavGraphBuilder.homeScreen(
    onClickAddCategory: () -> Unit,
    onClickAddTodo: () -> Unit,
) {
    composable(HOME_ROUTE) {
        HomeScreen(
            onClickAddCategory = onClickAddCategory,
            onClickAddTodo = onClickAddTodo
        )
    }
}

fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = HOME_ROUTE,
        route = HOME_GRAPH_ROUTE
    ) {
        homeScreen(
            onClickAddCategory = navController::navigateToAddCategory,
            onClickAddTodo = navController::navigateToAddTodo
        )
        addTodoScreen()
        addCategoryScreen(
            popBackStack = navController::popBackStack
        )
    }
}
