package com.example.home.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.category.AddCategoryScreen

const val ADD_CATEGORY_ROUTE = "addCategory"

internal fun NavController.navigateToAddCategory() {
    navigate(ADD_CATEGORY_ROUTE)
}

internal fun NavGraphBuilder.addCategoryScreen(
    popBackStack: () -> Unit
) {
    composable(ADD_CATEGORY_ROUTE) {
        AddCategoryScreen(
            onClickBack = popBackStack
        )
    }
}
