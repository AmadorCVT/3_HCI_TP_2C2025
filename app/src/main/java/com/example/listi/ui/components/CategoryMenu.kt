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
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableFilterMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    onItemClick: (String?) -> Unit, // puede ser null si se deselecciona
    onFixedButtonClick: () -> Unit,
) {
    // 🔹 Estado interno que guarda la categoría seleccionada (solo una o ninguna)
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 Botón fijo a la izquierda
        IconButton(
            onClick = onFixedButtonClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filtros",
                tint = Color.Black
            )
        }

        // 🔹 Lista horizontal deslizable
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
                        // ✅ Si ya estaba seleccionada → deselecciona
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
    val backgroundColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFFF5F5F5)
    val borderColor = if (isSelected) Color(0xFF388E3C) else Color(0xFFE0E0E0)
    val textColor = if (isSelected) Color.White else Color.Black

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
