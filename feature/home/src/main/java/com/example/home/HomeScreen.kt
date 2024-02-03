package com.example.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Category

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState.value,
        viewModel::changeCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onCategorySelected: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) {
        Box(Modifier.padding(it)) {
            when (uiState) {
                is HomeUiState.Loading -> HomeContentLoading(uiState)
                is HomeUiState.Error -> HomeContentError(uiState)
                is HomeUiState.Success -> HomeContentSuccess(
                    uiState,
                    onCategorySelected,
                    {}
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
    onCategorySelected: (Int) -> Unit,
    onClickAddCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        CategoryTabs(
            categories = uiState.categories,
            selectedCategoryId = uiState.selectedCategoryId,
            onCategorySelected = onCategorySelected,
            onClickAddCategory = onClickAddCategory
        )
        LazyColumn {
            items(uiState.todos.size) { index ->
                Text(uiState.todos[index].title)
            }
        }
    }
}

@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelected: (Int) -> Unit,
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
                onClick = { onCategorySelected(category.id) }
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
