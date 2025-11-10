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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.ui.components.ProductCard
import com.example.listi.ui.components.ScrollableFilterMenu
import com.example.listi.viewModel.ProductViewModel

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel() // 👈 obtiene el VM automáticamente
) {
    // Estados observados del ViewModel
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // 🔹 Cargar productos una sola vez cuando se abre la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        errorMessage != null -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: $errorMessage")
        }

        else -> {
            val categories = listOf("Todos") + products.map { it.category.name }.distinct()

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                // 🔹 Menú de categorías arriba
                ScrollableFilterMenu(
                    items = categories,
                    onItemClick = { category -> selectedCategory = category },
                    onFixedButtonClick = { /* TODO: filtros avanzados */ }
                )

                // 🔹 Filtrar productos según categoría seleccionada
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
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    ProductsScreen()
}
