package com.example.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.example.model.Category
import com.example.model.Todo

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClickAddCategory: () -> Unit,
    onClickAddTodo: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is HomeUiEvent.NavigateToAddCategory -> onClickAddCategory()
                is HomeUiEvent.NavigateToAddTodo -> onClickAddTodo()
                is HomeUiEvent.CompletionChanged -> {
                    val result = snackBarHostState.showSnackbar(
                        message = it.todo.title + " を完了しました",
                        actionLabel = "元に戻す"
                    )

                    if (result == SnackbarResult.ActionPerformed)
                        viewModel.undoComplete(it.todo)
                }
            }
        }
    }

    HomeContent(
        uiState.value,
        snackBarHostState,
        onSelectCategory = viewModel::changeCategory,
        onClickAddCategory = viewModel::navigateToAddCategory,
        onClickAddTodo = viewModel::navigateToAddTodo,
        onClickTodoComplete = viewModel::complete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    snackBarHostState: SnackbarHostState,
    onSelectCategory: (Int) -> Unit,
    onClickAddCategory: () -> Unit,
    onClickTodoComplete: (Todo) -> Unit,
    onClickAddTodo: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
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
                is HomeUiState.Loading -> HomeContentLoading(uiState)
                is HomeUiState.Error -> HomeContentError(uiState)
                is HomeUiState.Success -> HomeContentSuccess(
                    uiState = uiState,
                    onSelectCategory = onSelectCategory,
                    onClickAddCategory = onClickAddCategory,
                    onClickTodoComplete = onClickTodoComplete,
                )
            }
        }
    }
}

@Composable
private fun HomeContentLoading(
    uiState: HomeUiState.Loading,
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
            items(uiState.todos.size) { index ->
                TodoItem(
                    todo = uiState.todos[index],
                    onClickTodoComplete = onClickTodoComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
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
