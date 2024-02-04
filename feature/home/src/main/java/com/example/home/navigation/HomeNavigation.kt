package com.example.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.home.HomeScreen
import com.example.home.category.navigation.addCategoryScreen
import com.example.home.category.navigation.navigateToAddCategory
import com.example.home.todo.navigation.addTodoScreen
import com.example.home.todo.navigation.detailTodoScreen
import com.example.home.todo.navigation.navigateToAddTodo
import com.example.home.todo.navigation.navigateToDetailTodo

const val HOME_GRAPH_ROUTE = "homeGraph"

internal const val HOME_ROUTE = "home"


fun NavGraphBuilder.homeScreen(
    onClickAddCategory: () -> Unit,
    onClickAddTodo: () -> Unit,
    onClickTodo: (Int) -> Unit
) {
    composable(HOME_ROUTE) {
        HomeScreen(
            onClickAddCategory = onClickAddCategory,
            onClickAddTodo = onClickAddTodo,
            onClickTodo = onClickTodo
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
            onClickAddTodo = navController::navigateToAddTodo,
            onClickTodo = navController::navigateToDetailTodo
        )
        addTodoScreen(
            popBackStack = navController::popBackStack
        )
        addCategoryScreen(
            popBackStack = navController::popBackStack
        )
        detailTodoScreen(
            popBackStack = navController::popBackStack
        )
    }
}
