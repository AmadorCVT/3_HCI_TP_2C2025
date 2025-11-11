package com.example.listi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.CreateCategoryRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider


class CategoryViewModelFactory(
    private val repository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadCategories()
    }

    fun loadCategories(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _categories.value = repository.getCategories(forceRefresh)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            try {
                // 1) Crear categoría
                repository.createCategory(CreateCategoryRequest(name = name))

                // 2) Recargar lista (asegura que queda actualizada)
                loadCategories(forceRefresh = true)

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al crear categoría"
            }
        }
    }
}
