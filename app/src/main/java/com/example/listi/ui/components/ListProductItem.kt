package com.example.listi.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import com.example.listi.ui.types.ShoppingListItem
@Composable
fun ProductRow(
    item: ShoppingListItem,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    var checked by remember { mutableStateOf(item.purchased) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        // Línea superior gris
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFDDDDDD))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkmark
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (checked) Color(0xFF4CAF50) else Color.Transparent)
                    .clickable {
                        checked = !checked
                        onCheckedChange?.invoke(checked)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Checked",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(1.5.dp, Color(0xFFAAAAAA), CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Texto producto
            Text(
                text = item.product.name,
                modifier = Modifier.weight(1f),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )

            // Cantidad
            Text(
                text = item.quantity.toString(),
                modifier = Modifier.width(40.dp),
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Unidad
            Text(
                text = item.unit,
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
