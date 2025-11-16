package com.example.listi.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.Category
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.example.listi.R

@Composable
fun ProductRow(
    item: ShoppingListItem,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {
    val checked = item.purchased

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
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Checkmark
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (checked) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                        .clickable {
                            onCheckedChange()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (checked) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.check_foreground),
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

                Spacer(Modifier.width(16.dp))

                // Texto producto
                Text(
                    text = item.product.name,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                item.quantity.toString(),
                modifier = Modifier.weight(0.5f),
                color = Color.DarkGray
            )

            Text(
                item.unit,
                modifier = Modifier.weight(0.5f),
                color = Color.DarkGray
            )

            IconButton(onClick = { onDelete() }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.delete_foreground),
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProductRowPreview() {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val dateString = sdf.format(Date())

    val sampleCategory = Category(
        id = 1,
        name = "Lácteos",
        createdAt = dateString,
        updatedAt = dateString
    )

    val sampleProduct = Product(
        id = 1,
        name = "Leche Entera",
        createdAt = dateString,
        updatedAt = dateString,
        category = sampleCategory
    )

    val sampleItem = ShoppingListItem(
        id = 1,
        unit = "L",
        quantity = 2,
        purchased = false,
        lastPurchasedAt = dateString,
        createdAt = Date().toString(),
        updatedAt = Date().toString(),
        product = sampleProduct
    )

    ProductRow(item = sampleItem, {}, {})
}