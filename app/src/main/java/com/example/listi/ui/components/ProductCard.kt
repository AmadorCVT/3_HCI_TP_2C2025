package com.example.listi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.listi.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ShoppingList
import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone

@Composable
fun ProductCard(
    product: Product,
    categories: List<Category>,
    onCategoryChange: (Product, Category) -> Unit,
    onEditClick: (Product) -> Unit,
    onDeleteClick:(Product)->Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(product.category) }

    var menuExpanded by remember { mutableStateOf(false) }

    val actions = listOf(
        ActionItem(
            label = stringResource(R.string.edit),
            onClick = { onEditClick(product) }
        ),
        ActionItem(
            label = stringResource(R.string.delete),
            onClick = { onDeleteClick(product) }
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
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
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1.2f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 28.dp)
                        .wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        selectedCategory?.name ?: "-",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
                                onCategoryChange(product, category)
                            }
                        )
                    }
                }
            }


            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.more_vert_foreground),
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                ReusedDropdownMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    actions = actions
                )
            }
        }
    }
}

@Preview
@Composable
fun ProductCardPreview() {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val dateString = sdf.format(Date())

    val sampleCategories = listOf(
        Category(1, "Lácteos", dateString, dateString),
        Category(2, "Panificados", dateString, dateString),
        Category(3, "Bebidas", dateString, dateString)
    )
    val sampleProduct = Product(
        id = 1,
        name = "Leche",
        category = sampleCategories[0],
        createdAt = dateString,
        updatedAt = dateString
    )
    ProductCard(
        product = sampleProduct,
        categories = sampleCategories,
        onCategoryChange = { _, _ -> },
        onEditClick = {},
        onDeleteClick = {},
    )
}
