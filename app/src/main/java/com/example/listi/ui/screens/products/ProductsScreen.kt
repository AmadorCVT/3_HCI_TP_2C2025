package com.example.listi.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.R
import com.example.listi.network.RetrofitInstance
import com.example.listi.ui.components.AddCategoryDialog
import com.example.listi.ui.components.ProductCard
import com.example.listi.ui.components.ScrollableFilterMenu
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.components.GreenAddButton
import com.example.listi.ui.components.ProductDialog
import com.example.listi.ui.screens.shoppingLists.ShoppingListsCards
import com.example.listi.ui.types.ProductRequest
import kotlinx.coroutines.launch


@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory()),
    categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(CategoryRepository(RetrofitInstance.categoryService))
    )
) {
    // Para mostrar errores
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val productRefreshTrigger  by viewModel.refreshTrigger.collectAsState()

    val categories by categoryViewModel.categories.collectAsState()
    val categoryError by categoryViewModel.errorMessage.collectAsState()
    val categoryRefreshTrigger  by categoryViewModel.refreshTrigger.collectAsState()

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    LaunchedEffect(productRefreshTrigger) {
        viewModel.loadProducts()
    }

    LaunchedEffect(categoryRefreshTrigger) {
        categoryViewModel.loadCategories()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage.toString())
            }
        }
    }

    LaunchedEffect(categoryError) {
        categoryError?.let {
            scope.launch {
                snackbarHostState.showSnackbar(categoryError.toString())
            }
        }
    }


    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

        else -> {
            when {
                showAddCategoryDialog ->
                    AddCategoryDialog(
                        onDismiss = { showAddCategoryDialog = false },
                        onConfirm = { newCategoryName ->
                            categoryViewModel.addCategory(newCategoryName)
                            showAddCategoryDialog = false
                        }
                    )
                showAddProductDialog ->
                    ProductDialog(
                        onDismissRequest = { showAddProductDialog = false },
                        onConfirmation = { productRequest ->
                            viewModel.createProduct(productRequest)
                            showAddProductDialog = false
                        },
                        categories = categories
                    )
            }
            val categoryNames = listOf("Todos") + categories.map { it.name }

            // Scaffold para el snackbar
            Scaffold(
                snackbarHost = { SnackbarHost( snackbarHostState) }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {

                    Column(Modifier.fillMaxSize()) {

                        ScrollableFilterMenu(
                            items = categoryNames,
                            onItemClick = { selectedCategory = it },
                            onFixedButtonClick = { showAddCategoryDialog = true }
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
                                    categories = categories,
                                    onCategoryChange = { prod, newCat ->
                                        viewModel.updateProductCategory(prod, newCat)
                                    },
                                    onMenuClick = {  }
                                )
                            }
                        }
                    }

                    GreenAddButton(
                        { showAddProductDialog = true },
                        modifier
                    )
                }
            }

        }
    }

}
