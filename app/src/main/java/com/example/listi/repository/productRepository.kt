package com.example.listi.repository

import com.example.listi.data.api.ProductService
import com.example.listi.ui.types.CreateProductRequest
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.UpdateProductRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ProductRepository(private val api: ProductService) {

    private var cachedProducts: List<Product>? = null

    suspend fun getProducts(forceRefresh: Boolean = false): List<Product> {
        return withContext(Dispatchers.IO) {
            if (cachedProducts != null && !forceRefresh) {
                return@withContext cachedProducts!!
            }

            val response = api.getProducts().awaitResponse()
            if (response.isSuccessful) {
                val body = response.body()
                val products = body?.data ?: emptyList()
                cachedProducts = products
                products
            } else {
                throw Exception("Error al obtener productos: ${response.code()}")
            }
        }
    }

    suspend fun createProduct(request: CreateProductRequest): Product {
        return withContext(Dispatchers.IO) {
            val response = api.createProduct(request).awaitResponse()
            if (response.isSuccessful) {
                val product = response.body()!!
                // Actualizo cache local
                cachedProducts = (cachedProducts ?: emptyList()) + product
                product
            } else {
                throw Exception("Error al crear producto: ${response.code()}")
            }
        }
    }

    suspend fun updateProduct(id: Int, request: UpdateProductRequest): Product {
        return withContext(Dispatchers.IO) {
            val response = api.updateProduct(id, request).awaitResponse()
            if (response.isSuccessful) {
                val updated = response.body()!!
                cachedProducts = cachedProducts?.map {
                    if (it.id == id) updated else it
                }
                updated
            } else {
                throw Exception("Error al actualizar producto: ${response.code()}")
            }
        }
    }

    suspend fun deleteProduct(id: Int) {
        return withContext(Dispatchers.IO) {
            val response = api.deleteProduct(id).awaitResponse()
            if (response.isSuccessful) {
                cachedProducts = cachedProducts?.filterNot { it.id == id }
            } else {
                throw Exception("Error al eliminar producto: ${response.code()}")
            }
        }
    }
}
