package com.example.home

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testing.model.myTaskCategory
import com.example.testing.roborazzi.DefaultRoborazziRule
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.Pixel7)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = DefaultRoborazziRule

    @Test
    fun homeContent_success() {
        val uiState = HomeUiState.Success(
            uncompletedTodos = emptyList(),
            completedTodos = emptyList(),
            categories = listOf(myTaskCategory),
            selectedCategoryId = 1
        )
        val snackBarHostState = SnackbarHostState()
        composeTestRule.setContent {
            MaterialTheme {
                HomeContent(
                    uiState = uiState,
                    snackBarHostState = snackBarHostState,
                    onSelectCategory = {},
                    onClickAddCategory = {},
                    onClickTodoComplete = {},
                    onClickAddTodo = {},
                    onConfirmAlertDialog = {},
                    onDismissAlertDialog = {},
                    onDismissDropDownMenu = {},
                    onClickDeleteCategory = {},
                    onClickMoreButton = {},
                    onClickDisplayableCompletedTodos = {},
                    onClickTodo = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
