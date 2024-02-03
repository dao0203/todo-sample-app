package com.example.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.home.HomeScreen
import com.example.home.category.navigation.addCategoryScreen
import com.example.home.todo.navigation.addTodoScreen

const val HOME_GRAPH_ROUTE = "home"

internal const val HOME_ROUTE = "home"


fun NavGraphBuilder.homeScreen() {
    composable(HOME_GRAPH_ROUTE) {
        HomeScreen()
    }
}

fun NavGraphBuilder.homeNavGraph() {
    navigation(
        startDestination = HOME_GRAPH_ROUTE,
        route = HOME_ROUTE
    ) {
        homeScreen()
        addTodoScreen()
        addCategoryScreen()
    }
}