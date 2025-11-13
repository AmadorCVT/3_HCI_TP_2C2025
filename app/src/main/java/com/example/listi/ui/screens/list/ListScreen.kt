package com.example.listi.ui.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ShoppingListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
@Composable
fun ListScreenView(items: List<ShoppingListItem>) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp,64.dp),
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

            LazyColumn {
                item { HeaderRow() }
                items(items) { item ->
                    ProductRow(item = item)
                }
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
    val category1 = Category(1, "Panadería", "", dateFormat.format(Date()))
    val category2 = Category(2, "Almacén", "", dateFormat.format(Date()))

    val product1 = Product(
        id = 1,
        name = "Pan",
        createdAt = dateFormat.format(Date()),
        updatedAt = dateFormat.format(Date()),
        category = category1
    )

    val product2 = Product(
        id = 2,
        name = "Sal",
        createdAt = dateFormat.format(Date()),
        updatedAt = dateFormat.format(Date()),
        category = category2
    )

    val sampleItems = listOf(
        ShoppingListItem(1, "Kg", 1, true, "Ayer", Date(), Date(), product1),
        ShoppingListItem(2, "g", 500, false, "Hoy", Date(), Date(), product2),
    )
    ListScreenView(items = sampleItems)
}
