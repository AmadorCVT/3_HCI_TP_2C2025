package com.example.listi.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.CreateCategoryRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger = _refreshTrigger.asStateFlow()

    init {
        loadCategories()
    }

    fun clearError() {
        _errorMessage.value = null
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
                repository.createCategory(CreateCategoryRequest(name = name))
                loadCategories(forceRefresh = true)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al crear categoría"
            }
        }

        _refreshTrigger.value++
    }


    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                repository.deleteCategory(id)
                loadCategories(forceRefresh = true) // refrescar lista

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al borrar categoría"
            } finally {
                _isLoading.value = false
            }
        }

        _refreshTrigger.value++
    }
}
