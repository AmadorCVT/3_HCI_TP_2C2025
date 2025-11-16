package com.example.listi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.Green
import com.example.listi.ui.theme.White

@Composable
fun ScrollableFilterMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    onItemClick: (String?) -> Unit, // puede ser null si se deselecciona
    onFixedButtonClick: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onFixedButtonClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Green,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar categoría",
                tint = White
            )
        }

        LazyRow(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { label ->
                val isSelected = selectedCategory == label

                FilterChip(
                    text = label,
                    isSelected = isSelected,
                    onClick = {
                        selectedCategory = if (isSelected) null else label
                        onItemClick(selectedCategory)
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Green else Color.Transparent
    val borderColor = if (isSelected) Green else DarkGrey
    val textColor = if (isSelected) White else DarkGrey

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
    MaterialTheme {
        ScrollableFilterMenu(
            items = listOf("Lácteos", "Verduras", "Panificados", "Carnes", "Bebidas"),
            onItemClick = {},
            onFixedButtonClick = {}
        )
    }
}