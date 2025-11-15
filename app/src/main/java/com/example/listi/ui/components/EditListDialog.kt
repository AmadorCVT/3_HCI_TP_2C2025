package com.example.listi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listi.ui.types.ShoppingListRequest

@Composable
fun EditShoppingListDialog(
    listName: String,
    recurring: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (ShoppingListRequest) -> Unit
) {
    var name by remember { mutableStateOf(listName) }
    var isRecurring by remember { mutableStateOf(recurring) }

    val background = MaterialTheme.colorScheme.background
    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val green = MaterialTheme.colorScheme.tertiary

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = background,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Editar lista",
                color = primary
            )
        },
        text = {
            Column {

                Text(
                    text = "Nombre",
                    color = primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = surface,
                        unfocusedContainerColor = surface,
                        focusedIndicatorColor = primary,
                        unfocusedIndicatorColor = primary,
                        cursorColor = primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = isRecurring,
                        onCheckedChange = { isRecurring = it },
                        colors = androidx.compose.material3.CheckboxDefaults.colors(
                            checkedColor = primary,
                            uncheckedColor = primary
                        )
                    )
                    Text("Recurrente", color = primary)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        ShoppingListRequest(
                            name = name,
                            description = "",
                            recurring = isRecurring
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = green,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text("CONFIRMAR")
            }
        },
        dismissButton = {
            TextButton(onDismissRequest) {
                Text(
                    text = "CANCELAR",
                    color = primary
                )
            }
        }
    )
}
