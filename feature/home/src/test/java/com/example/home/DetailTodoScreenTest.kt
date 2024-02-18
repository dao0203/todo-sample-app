package com.example.home

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.home.todo.DetailTodoContent
import com.example.home.todo.DetailTodoUiState
import com.example.testing.model.myTodo
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
class DetailTodoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = DefaultRoborazziRule

    @Test
    fun detailTodoContent_success() {
        val uiState = DetailTodoUiState.Success(
            todo = myTodo,
        )
        composeTestRule.setContent {
            MaterialTheme {
                DetailTodoContent(
                    uiState = uiState,
                    onClickBack = {},
                    onClickDropDownMenu = {},
                    onClickDeleteTodo = {},
                    onDismissRequestDropDownMenu = {},
                    onClickCompleteTodo = {},
                    onClickUndoCompleteTodo = {},
                    onConfirmDialog = {},
                    onDismissDialog = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}