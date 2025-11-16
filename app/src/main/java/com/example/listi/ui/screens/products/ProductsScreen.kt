package com.example.listi.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.R
import com.example.listi.network.RetrofitInstance
import com.example.listi.ui.components.AddCategoryDialog
import com.example.listi.ui.components.ProductCard
import com.example.listi.ui.components.ScrollableFilterMenu
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.components.DeleteDialog
import com.example.listi.ui.components.GreenAddButton
import com.example.listi.ui.components.ProductDialog
import com.example.listi.ui.screens.shoppingLists.ShoppingListsCards
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ProductRequest
import com.example.listi.ui.types.ShoppingList
import kotlinx.coroutines.launch


@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory()),
    categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(CategoryRepository(RetrofitInstance.categoryService))
    )
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val productRefreshTrigger  by viewModel.refreshTrigger.collectAsState()
    val connectionError = stringResource(R.string.error_connection)
    val itemError = stringResource(R.string.error_item)

    val categories by categoryViewModel.categories.collectAsState()
    val categoryError by categoryViewModel.errorMessage.collectAsState()
    val categoryRefreshTrigger  by categoryViewModel.refreshTrigger.collectAsState()

    var productToModify by remember { mutableStateOf<Product?>(null) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showEditProductDialog by remember { mutableStateOf(false) }
    var showDeleteProductDialog by remember { mutableStateOf(false) }

    LaunchedEffect(productRefreshTrigger) {
        viewModel.loadProducts()
    }

    LaunchedEffect(categoryRefreshTrigger) {
        categoryViewModel.loadCategories()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            var message = connectionError

            when(errorMessage) {
                "409" ->  message = itemError
                else -> connectionError
            }

            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message
                )
            }
        }
        viewModel.clearError()
    }

    LaunchedEffect(categoryError) {
        categoryError?.let {
            var message = connectionError

            when(categoryError) {
                "409" ->  message = itemError
                else -> connectionError
            }

            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message
                )
            }
        }
        categoryViewModel.clearError()
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
                showEditProductDialog ->
                    productToModify?.let {
                        ProductDialog(
                            product = productToModify,
                            onDismissRequest = { showEditProductDialog = false },
                            onConfirmation = { productRequest ->
                                viewModel.updateProduct(productToModify!!.id, productRequest)
                                showEditProductDialog= false
                                productToModify = null
                            },
                            categories = categories
                        )
                    }
                showDeleteProductDialog ->
                    productToModify?.let {
                        DeleteDialog(
                            name = productToModify!!.name,
                            onDismiss = { showDeleteProductDialog = false },
                            onConfirm = {
                                viewModel.deleteProduct(productToModify!!.id)
                                showDeleteProductDialog= false
                                productToModify = null
                            }
                        )
                    }
            }
            val categories =  categories.map { it }

            // Scaffold para el snackbar
            Scaffold(
                snackbarHost = { SnackbarHost( snackbarHostState) }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {

                    Column(Modifier.fillMaxSize()) {

                        ScrollableFilterMenu(
                            items = categories,
                            onItemClick = { selectedCategory = it?.name },
                            onFixedButtonClick = { showAddCategoryDialog = true },
                            onDeleteCategory ={categoryViewModel.deleteCategory(it)}
                        )

                        val filteredProducts = if (selectedCategory != null && selectedCategory != "Todos")
                            products.filter { it.category?.name == selectedCategory }
                        else products

                        val screenWidth = maxWidthDp()
                        val columns = calculateColumns(screenWidth)

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(filteredProducts) { product ->
                                ProductCard(
                                    product = product,
                                    categories = categories,
                                    onCategoryChange = { prod, newCat ->
                                        viewModel.updateProductCategory(prod, newCat)
                                    },
                                    onEditClick = {
                                        productToModify = it
                                        showEditProductDialog = true
                                    },
                                    onDeleteClick = {
                                        productToModify = it
                                        showDeleteProductDialog = true
                                    }
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

@Composable
fun maxWidthDp(): Int {
    return LocalConfiguration.current.screenWidthDp
}

fun calculateColumns(screenWidthDp: Int): Int {
    val minCardWidth = 240 // igual que Friends
    return maxOf(1, screenWidthDp / minCardWidth)
}