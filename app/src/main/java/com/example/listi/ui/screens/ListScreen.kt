package com.example.listi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.components.ProductRow
import com.example.listi.ui.components.WhiteBoxWithText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit

@Composable
fun ListScreenView() {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // === Caja de texto arriba del todo ===
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Nombre") },
            trailingIcon = {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // === Encabezado verde ===


        // === WhiteBox con los productos ===
        WhiteBoxWithText(
            text = "",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {

            Column {
                    HeaderRow()
                ProductRow(name = "Pan", quantity = "1", unit = "Kg", initiallyChecked = true)
                ProductRow(name = "Sal", quantity = "500", unit = "g")
            }
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Producto",
            color = Color(0xFF2E7D32), // verde tipo material
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1.2f)
        )
        Text(
            text = "Cant.",
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = "Unidad",
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }

    Divider(
        color = Color(0xFFBDBDBD),
        thickness = 1.dp,
        modifier = Modifier.padding(top = 2.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreenView()
}
