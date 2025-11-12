package com.example.listi.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.network.RetrofitInstance
import com.example.listi.ui.components.AddCategoryDialog
import com.example.listi.ui.components.ProductCard
import com.example.listi.ui.components.ScrollableFilterMenu
import com.example.listi.repository.CategoryRepository


@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory()),
    categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(CategoryRepository(RetrofitInstance.categoryService))
    )
) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val categories by categoryViewModel.categories.collectAsState()
    val categoryError by categoryViewModel.errorMessage.collectAsState()

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var showAddCategoryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
        categoryViewModel.loadCategories() // ✅ Cargar categorías
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

        errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Error: $errorMessage")
        }

        categoryError != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Error categorías: $categoryError")
        }

        else -> {
            val categoryNames = listOf("Todos") + categories.map { it.name }

            Column(Modifier.fillMaxSize()) {

                ScrollableFilterMenu(
                    items = categoryNames,
                    onItemClick = { selectedCategory = it },
                    onFixedButtonClick = { showAddCategoryDialog = true } // ✅ Abrir diálogo
                )

                val filteredProducts = if (selectedCategory != null && selectedCategory != "Todos")
                    products.filter { it.category.name == selectedCategory }
                else products

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            categories = categories, // ✅ Ahora vienen del CategoryViewModel
                            onCategoryChange = { prod, newCat ->
                                viewModel.updateProductCategory(prod, newCat)
                            },
                            onMenuClick = {  }
                        )
                    }
                }
            }
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { newCategoryName ->
                categoryViewModel.addCategory(newCategoryName) // ✅ Ahora se llama al CategoryViewModel
                showAddCategoryDialog = false
            }
        )
    }
}
