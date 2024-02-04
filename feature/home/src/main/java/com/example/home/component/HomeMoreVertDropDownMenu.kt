package com.example.home.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeMoreVertDropDownMenu(
    expanded: Boolean,
    onClickButton: () -> Unit,
    onDismissRequest: () -> Unit,
    onClickDeleteCategory: () -> Unit,
) {
    IconButton(
        onClick = onClickButton,
    ) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("カテゴリを削除") },
            onClick = onClickDeleteCategory
        )
    }
}