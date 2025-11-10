package com.example.listi.repository

import com.example.listi.services.CategoryService
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.CreateCategoryRequest
import com.example.listi.ui.types.UpdateCategoryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val api: CategoryService) {

    private var cachedCategories: List<Category>? = null

    suspend fun getCategories(forceRefresh: Boolean = false): List<Category> {
        return withContext(Dispatchers.IO) {
            if (cachedCategories != null && !forceRefresh) {
                return@withContext cachedCategories!!
            }

            val response = api.getCategories()
            if (response.isSuccessful) {
                val categories = response.body()?.data ?: emptyList()
                cachedCategories = categories
                categories
            } else {
                throw Exception("Error al obtener categorías: ${response.code()}")
            }
        }
    }

    suspend fun createCategory(request: CreateCategoryRequest): Category {
        return withContext(Dispatchers.IO) {
            val response = api.createCategory(request)
            if (response.isSuccessful) {
                val createdResponse = response.body()!!
                // convertimos el CategoryResponse a Category manualmente
                val created = Category(
                    id = createdResponse.id,
                    name = createdResponse.name,
                    metadata = createdResponse.metadata,
                    createdAt = java.util.Date(),
                    updatedAt = java.util.Date()
                )
                cachedCategories = (cachedCategories ?: emptyList()) + created
                created
            } else {
                throw Exception("Error al crear categoría: ${response.code()}")
            }
        }
    }

    suspend fun getCategoryById(id: Int): Category {
        return withContext(Dispatchers.IO) {
            val response = api.getCategoryById(id)
            if (response.isSuccessful) {
                val result = response.body()!!
                Category(
                    id = result.id,
                    name = result.name,
                    metadata = result.metadata,
                    createdAt = java.util.Date(),
                    updatedAt = java.util.Date()
                )
            } else {
                throw Exception("Error al obtener categoría: ${response.code()}")
            }
        }
    }

    suspend fun updateCategory(id: Int, request: UpdateCategoryRequest): Category {
        return withContext(Dispatchers.IO) {
            val response = api.updateCategory(id, request)
            if (response.isSuccessful) {
                val result = response.body()!!
                val updated = Category(
                    id = result.id,
                    name = result.name,
                    metadata = result.metadata,
                    createdAt = java.util.Date(),
                    updatedAt = java.util.Date()
                )
                cachedCategories = cachedCategories?.map {
                    if (it.id == id) updated else it
                }
                updated
            } else {
                throw Exception("Error al actualizar categoría: ${response.code()}")
            }
        }
    }

    suspend fun deleteCategory(id: Int) {
        return withContext(Dispatchers.IO) {
            val response = api.deleteCategory(id)
            if (response.isSuccessful) {
                cachedCategories = cachedCategories?.filterNot { it.id == id }
            } else {
                throw Exception("Error al eliminar categoría: ${response.code()}")
            }
        }
    }
}
