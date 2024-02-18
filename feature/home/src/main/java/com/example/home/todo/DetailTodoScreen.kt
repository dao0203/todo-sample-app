package com.example.home.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.home.component.ConfirmDeleteDialog
import com.example.home.todo.component.DetailMoreVertDropDownMenu

@Composable
fun DetailTodoScreen(
    viewModel: DetailTodoViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is DetailTodoUiEvent.NavigateToBack -> onClickBack()
            }
        }
    }

    DetailTodoContent(
        uiState = uiState.value,
        onClickBack = viewModel::back,
        onClickDropDownMenu = viewModel::showDropDownMenu,
        onClickDeleteTodo = viewModel::showDeleteTodoDialog,
        onDismissRequestDropDownMenu = viewModel::dismissDropDownMenu,
        onClickCompleteTodo = viewModel::completeTodo,
        onClickUndoCompleteTodo = viewModel::undoCompleteTodo,
        onConfirmDialog = viewModel::deleteTodo,
        onDismissDialog = viewModel::dismissDeleteTodoDialog,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailTodoContent(
    uiState: DetailTodoUiState,
    onClickBack: () -> Unit,
    onClickDropDownMenu: () -> Unit,
    onClickDeleteTodo: () -> Unit,
    onDismissRequestDropDownMenu: () -> Unit,
    onClickCompleteTodo: () -> Unit,
    onClickUndoCompleteTodo: () -> Unit,
    onConfirmDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState is DetailTodoUiState.Success && uiState.showDeleteTodoDialog) {
        ConfirmDeleteDialog(
            title = "削除",
            text = "復元することはできません。",
            onConfirm = onConfirmDialog,
            onDismiss = onDismissDialog,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("詳細") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (uiState is DetailTodoUiState.Success && uiState.todo.isCompleted)
                        TextButton(onClick = onClickUndoCompleteTodo) {
                            Text("未完了にする")
                        }
                    else
                        TextButton(onClick = onClickCompleteTodo) {
                            Text("完了にする")
                        }
                    Spacer(Modifier.width(4.dp))
                    DetailMoreVertDropDownMenu(
                        expanded = uiState.expandedDropDownMenu,
                        onClickButton = onClickDropDownMenu,
                        onClickDeleteTodo = onClickDeleteTodo,
                        onDismissRequest = onDismissRequestDropDownMenu,
                    )
                }
            )
        },
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (uiState) {
                is DetailTodoUiState.Loading -> DetailTodoContentLoading()
                is DetailTodoUiState.Success -> DetailTodoContentSuccess(
                    uiState = uiState,
                )
            }
        }
    }
}

@Composable
internal fun DetailTodoContentLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }

}

@Composable
internal fun DetailTodoContentSuccess(
    uiState: DetailTodoUiState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = uiState.todo.title)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = uiState.todo.description)
        }
    }
}
