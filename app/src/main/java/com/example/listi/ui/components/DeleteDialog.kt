package com.example.listi.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.listi.R

@Composable
fun DeleteDialog(
    name: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val delete_confirm = stringResource(R.string.delete_confirmation)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete),
                color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium)
        },
        text = {
            Text(
                color = MaterialTheme.colorScheme.secondary,
                text = "$delete_confirm \"$name\"?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
