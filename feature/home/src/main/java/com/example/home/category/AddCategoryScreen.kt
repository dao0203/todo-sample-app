package com.example.home.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddCategoryScreen(
    viewModel: AddCategoryViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is AddCategoryUiEvent.TransitionBack -> onClickBack()
            }
        }
    }

    AddCategoryContent(
        uiState.value,
        viewModel::changeName,
        viewModel::addCategory,
        viewModel::back,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCategoryContent(
    uiState: AddCategoryUiState,
    onChangeName: (String) -> Unit,
    onCompleteName: () -> Unit,
    onClickBack: () -> Unit,
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
                        onClick = onCompleteName,
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
                value = uiState.name,
                onValueChange = onChangeName,
                label = { Text("カテゴリ名") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}