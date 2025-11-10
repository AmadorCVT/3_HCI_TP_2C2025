package com.example.listi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.ui.components.ProductCard
import com.example.listi.ui.components.ScrollableFilterMenu
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import java.util.*

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier
) {
    // 🔹 Datos fijos (hardcodeados)
    val categories = listOf("Todos", "Bebidas", "Lácteos", "Snacks", "Higiene")

    val products = listOf(
        Product(
            id = 1,
            name = "Coca-Cola",
            description = "Gaseosa de 500ml",
            category = Category(
                id = 1,
                name = "Bebidas",
                createdAt = Date(),
                updatedAt = Date()
            ),
            createdAt = Date(),
            updatedAt = Date()
        ),
        Product(
            id = 2,
            name = "Leche La Serenísima",
            description = "Entera 1L",
            category = Category(
                id = 2,
                name = "Lácteos",
                createdAt = Date(),
                updatedAt = Date()
            ),
            createdAt = Date(),
            updatedAt = Date()
        ),
        Product(
            id = 3,
            name = "Papas Lays",
            description = "Sabor clásico",
            category = Category(
                id = 3,
                name = "Snacks",
                createdAt = Date(),
                updatedAt = Date()
            ),
            createdAt = Date(),
            updatedAt = Date()
        ),
        Product(
            id = 4,
            name = "Jabón Dove",
            description = "Hidratante 90g",
            category = Category(
                id = 4,
                name = "Higiene",
                createdAt = Date(),
                updatedAt = Date()
            ),
            createdAt = Date(),
            updatedAt = Date()
        )
    )

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // 🔹 UI principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        // 🔹 Menú de categorías arriba
        ScrollableFilterMenu(
            items = categories,
            onItemClick = { category ->
                selectedCategory = category
            },
            onFixedButtonClick = { /* Acción del botón filtro */ }
        )

        // 🔹 Filtrado dinámico
        val filteredProducts = if (selectedCategory != null && selectedCategory != "Todos") {
            products.filter { it.category.name == selectedCategory }
        } else {
            products
        }

        // 🔹 Lista de productos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredProducts) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    ProductsScreen()
}
