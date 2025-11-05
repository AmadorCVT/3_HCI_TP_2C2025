package com.example.listi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReusedDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    actions: List<ActionItem>
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .background(Color(0xFFAEC896), shape = RoundedCornerShape(6.dp))
    ) {
        actions.forEachIndexed { index, action ->
            DropdownMenuItem(
                text = { Text(action.label, fontSize = 16.sp, color = Color.Black) },
                onClick = {
                    action.onClick()
                    onDismiss()
                }
            )
            if (index != actions.lastIndex) {
                Divider(
                    color = Color(0xFF6A8F5C),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }
}
data class ActionItem(
    val label: String,
    val onClick: () -> Unit
)
