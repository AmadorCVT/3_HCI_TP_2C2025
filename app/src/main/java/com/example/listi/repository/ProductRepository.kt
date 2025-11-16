package com.example.listi.repository

import com.example.listi.data.api.ProductService
import com.example.listi.ui.types.ProductRequest
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ProductResponse
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
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun createProduct(request: ProductRequest): Product {
        return withContext(Dispatchers.IO) {
            val response = api.createProduct(request).awaitResponse()
            if (response.isSuccessful) {
                val product = response.body()!!
                // Actualizo cache local
                cachedProducts = (cachedProducts ?: emptyList()) + product
                product
            } else {
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun updateProduct(id: Int, request: ProductRequest): ProductResponse {
        return withContext(Dispatchers.IO) {
            val response = api.updateProduct(id, request).awaitResponse()
            if (response.isSuccessful) {
                val updated = response.body()!!
                cachedProducts = cachedProducts?.map {
                    if (it.id == id) updated.product else it
                }
                updated
            } else {
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun deleteProduct(id: Int) {
        return withContext(Dispatchers.IO) {
            val response = api.deleteProduct(id).awaitResponse()
            if (response.isSuccessful) {
                cachedProducts = cachedProducts?.filterNot { it.id == id }
            } else {
                throw Exception(response.code().toString())
            }
        }
    }
}
