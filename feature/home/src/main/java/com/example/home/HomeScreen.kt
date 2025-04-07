package com.example.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.home.component.CompletedTodoItem
import com.example.home.component.ConfirmDeleteDialog
import com.example.home.component.HomeMoreVertDropDownMenu
import com.example.home.component.UncompletedTodoItem
import com.example.model.Category
import com.example.model.Todo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClickAddCategory: () -> Unit,
    onClickAddTodo: () -> Unit,
    onClickTodo: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is HomeUiEvent.NavigateToAddCategory -> onClickAddCategory()
                is HomeUiEvent.NavigateToAddTodo -> onClickAddTodo()
                is HomeUiEvent.NavigateToDetail -> onClickTodo(it.todoId)
                is HomeUiEvent.ShowSnackbar -> snackBarHostState.showSnackbar(
                    message = it.message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    HomeContent(
        uiState.value,
        snackBarHostState,
        onSelectCategory = viewModel::changeCategory,
        onClickAddCategory = viewModel::navigateToAddCategory,
        onClickAddTodo = viewModel::navigateToAddTodo,
        onClickTodoComplete = viewModel::complete,
        onConfirmAlertDialog = viewModel::deleteCategory,
        onDismissAlertDialog = viewModel::dismissConfirmDeleteDialog,
        onDismissDropDownMenu = viewModel::dismissDropDownMenu,
        onClickDeleteCategory = viewModel::showConfirmDeleteDialog,
        onClickMoreButton = viewModel::expandDropDownMenu,
        onClickDisplayableCompletedTodos = viewModel::toggleShowCompletedTodos,
        onClickTodo = viewModel::navigateToDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    uiState: HomeUiState,
    snackBarHostState: SnackbarHostState,
    onSelectCategory: (Int) -> Unit,
    onClickAddCategory: () -> Unit,
    onClickTodoComplete: (Todo) -> Unit,
    onClickAddTodo: () -> Unit,
    onConfirmAlertDialog: () -> Unit,
    onDismissAlertDialog: () -> Unit,
    onDismissDropDownMenu: () -> Unit,
    onClickDeleteCategory: () -> Unit,
    onClickMoreButton: () -> Unit,
    onClickDisplayableCompletedTodos: () -> Unit,
    onClickTodo: (Todo) -> Unit
) {
    if (uiState.showConfirmDeleteDialog) {
        ConfirmDeleteDialog(
            title = "カテゴリを削除",
            text = "カテゴリを削除すると、カテゴリに属するタスクも削除され、復元することはできません",
            onConfirm = onConfirmAlertDialog,
            onDismiss = onDismissAlertDialog
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("home") },
                actions = {
                    HomeMoreVertDropDownMenu(
                        expanded = uiState.expandedDropDownMenu,
                        onDismissRequest = onDismissDropDownMenu,
                        onClickDeleteCategory = onClickDeleteCategory,
                        onClickButton = onClickMoreButton
                    )
                    Spacer(Modifier.width(4.dp))
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            IconButton(
                onClick = onClickAddTodo,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add todo",
                )
            }
        }
    ) {
        Box(Modifier.padding(it)) {
            when (uiState) {
                is HomeUiState.Loading -> HomeContentLoading()
                is HomeUiState.Error -> HomeContentError(uiState)
                is HomeUiState.Success -> HomeContentSuccess(
                    uiState = uiState,
                    onSelectCategory = onSelectCategory,
                    onClickAddCategory = onClickAddCategory,
                    onClickTodoComplete = onClickTodoComplete,
                    onClickDisplayableCompletedTodos = onClickDisplayableCompletedTodos,
                    onClickTodo = onClickTodo
                )
            }
        }
    }
}

@Composable
private fun HomeContentLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeContentError(
    uiState: HomeUiState.Error,
    modifier: Modifier = Modifier
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = uiState.errorMessage,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun HomeContentSuccess(
    uiState: HomeUiState.Success,
    onSelectCategory: (Int) -> Unit,
    onClickAddCategory: () -> Unit,
    onClickTodoComplete: (Todo) -> Unit,
    onClickDisplayableCompletedTodos: () -> Unit,
    onClickTodo: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        CategoryTabs(
            categories = uiState.categories,
            selectedCategoryId = uiState.selectedCategoryId,
            onSelectCategory = onSelectCategory,
            onClickAddCategory = onClickAddCategory
        )
        LazyColumn {
            items(uiState.uncompletedTodos.size) { index ->
                UncompletedTodoItem(
                    todo = uiState.uncompletedTodos[index],
                    onClick = onClickTodo,
                    onClickCheckbox = onClickTodoComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            item {
                HorizontalDivider()
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClickDisplayableCompletedTodos() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "完了済みタスク",
                        modifier = Modifier.weight(1f)
                    )
                    if (uiState.showCompletedTodos)
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null
                        )
                    else
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    Spacer(Modifier.width(12.dp))
                }
            }
            if (uiState.showCompletedTodos) {
                items(uiState.completedTodos.size) { index ->
                    CompletedTodoItem(
                        todo = uiState.completedTodos[index],
                        onClick = onClickTodo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategoryId: Int,
    onSelectCategory: (Int) -> Unit,
    onClickAddCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = categories.indexOfFirst { it.id == selectedCategoryId }
    ) {
        categories.forEach { category ->
            Tab(
                selected = category.id == selectedCategoryId,
                onClick = { onSelectCategory(category.id) }
            ) {
                Text(
                    category.name,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        Tab(
            selected = false,
            onClick = onClickAddCategory,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add category",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
