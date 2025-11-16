package com.example.listi.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteCategoryDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Eliminar categoría",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(text = "¿Está seguro que quiere borrar la categoría \"$categoryName\"?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = Color.White
                )
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
