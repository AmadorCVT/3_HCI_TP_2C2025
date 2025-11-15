package com.example.listi.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.CategoryRepository
import com.example.listi.repository.ProductRepository
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.CategoryReference
import com.example.listi.ui.types.UpdateProductRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val productApi = RetrofitInstance.productService
        val categoryApi = RetrofitInstance.categoryService
        return ProductViewModel(
            productRepository = ProductRepository(productApi),
            categoryRepository = CategoryRepository(categoryApi)
        ) as T
    }
}

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger = _refreshTrigger.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _products.value = productRepository.getProducts()
                _categories.value = categoryRepository.getCategories()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProductCategory(product: Product, newCategory: Category) {
        viewModelScope.launch {
            try {
                productRepository.updateProduct(
                    id = product.id,
                    request = UpdateProductRequest(
                        name = product.name,
                        category = CategoryReference(newCategory.id)
                    )
                )
                loadProducts()
            } catch (_: Exception) {}
        }
        _refreshTrigger.value++
    }
}
