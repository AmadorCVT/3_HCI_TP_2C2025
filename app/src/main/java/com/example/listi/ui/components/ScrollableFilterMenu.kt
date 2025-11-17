package com.example.listi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import com.example.listi.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.Green
import com.example.listi.ui.theme.White
import com.example.listi.ui.types.Category

@Composable
fun ScrollableFilterMenu(
    modifier: Modifier = Modifier,
    items: List<Category>,
    onItemClick: (Category?) -> Unit,
    onFixedButtonClick: () -> Unit,
    onDeleteCategory: (Int) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    var deleteMode by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    deleteMode = false
                    onFixedButtonClick()
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Green, shape = CircleShape)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.add),
                    modifier = Modifier.size(24.dp),
                    tint = White,
                    contentDescription = "Agregar categoría"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))


            IconButton(
                onClick = { deleteMode = !deleteMode },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (deleteMode) Color(0xFFFFCDD2) else Color.Transparent,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.delete_foreground),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Borrar categoría",
                    tint = if (deleteMode)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { category ->
                    val isSelected = selectedCategory?.id == category.id

                    FilterChipWithDeleteBehavior(
                        text = category.name,
                        isSelected = isSelected,
                        deleteMode = deleteMode,
                        onClick = {
                            if (deleteMode) {
                                categoryToDelete = category
                                showDeleteDialog = true
                            } else {
                                selectedCategory = if (isSelected) null else category
                                onItemClick(selectedCategory)
                            }
                        }
                    )
                }
            }
        }


        if (showDeleteDialog && categoryToDelete != null) {
            DeleteCategoryDialog(
                categoryName = categoryToDelete!!.name,
                onDismiss = {
                    showDeleteDialog = false
                    categoryToDelete = null
                },
                onConfirm = {
                    onDeleteCategory(categoryToDelete!!.id)
                    showDeleteDialog = false
                    deleteMode = false
                    categoryToDelete = null
                }
            )
        }
    }
}

@Composable
private fun FilterChipWithDeleteBehavior(
    text: String,
    isSelected: Boolean,
    deleteMode: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        deleteMode -> Color.Transparent
        isSelected -> Green
        else -> Color.Transparent
    }
    val borderColor = when {
        deleteMode -> if (isSelected) Color.Red else DarkGrey
        isSelected -> Green
        else -> DarkGrey
    }
    val textColor = when {
        deleteMode -> if (isSelected) Color.Red else DarkGrey
        isSelected -> White
        else -> MaterialTheme.colorScheme.secondary
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .height(36.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollableFilterMenuPreview() {
    val sample = listOf(
        Category(1, "Lácteos", "", "", ""),
        Category(2, "Verduras", "null", "", ""),
        Category(3, "Panificados", "null", "", "")
    )

    ScrollableFilterMenu(
        items = sample,
        onItemClick = {},
        onFixedButtonClick = {},
        onDeleteCategory = {}
    )
}
