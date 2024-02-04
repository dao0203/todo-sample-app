package com.example.home.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ConfirmDeleteDialog(
    title: String? = null,
    text: String? = null,
    confirmText: String = "削除",
    dismissText: String = "キャンセル",
    icon: ImageVector = Icons.Default.Info,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = { Icon(imageVector = icon, contentDescription = null) },
        title = { title?.let { Text(it) } },
        text = { text?.let { Text(it) } },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}