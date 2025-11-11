package com.example.listi.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProductCard(
    product: Product,
    categories: List<Category>,
    onCategoryChange: (Product, Category) -> Unit,
    onMenuClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(product.category) }

    val formattedDate = remember(product.createdAt) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(product.createdAt)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 2.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = product.name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Selección de categoría
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text(selectedCategory.name, fontSize = 12.sp)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                expanded = false
                                selectedCategory = category
                                onCategoryChange(product, category) // ✅ Notificamos a ViewModel
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(formattedDate, fontSize = 11.sp)

            IconButton(onClick = { onMenuClick(product) }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
            }
        }
    }
}
