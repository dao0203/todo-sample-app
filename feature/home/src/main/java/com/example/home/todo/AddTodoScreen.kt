package com.example.home.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Category

@Composable
fun AddTodoScreen(
    viewModel: AddTodoViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is AddTodoUiEvent.TransitionBack -> onClickBack()
            }
        }
    }

    AddTodoContent(
        uiState.value,
        viewModel::changeTitle,
        viewModel::changeDescription,
        viewModel::changeCategory,
        viewModel::addTodo,
        viewModel::back,
        viewModel::toggleCategoryDropDown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTodoContent(
    uiState: AddTodoUiState,
    onChangeTitle: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onChangeCategory: (Category) -> Unit,
    onComplete: () -> Unit,
    onClickBack: () -> Unit,
    onClickCategoryTextField: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = { Text("カテゴリ追加") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onComplete,
                        enabled = uiState.enabledCompleteButton
                    ) {
                        Text("完了")
                    }
                }
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                value = uiState.title,
                onValueChange = onChangeTitle,
                label = { Text("タイトル") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = uiState.description,
                onValueChange = onChangeDescription,
                label = { Text("説明") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            CategoryDropDown(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = onChangeCategory,
                expanded = uiState.categoryDropDownExpanded,
                onExpandedChange = onClickCategoryTextField,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CategoryDropDown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        TextField(
            value = selectedCategory?.name ?: "",
            onValueChange = {},
            label = { Text("カテゴリ") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (it.isFocused) onExpandedChange() },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}