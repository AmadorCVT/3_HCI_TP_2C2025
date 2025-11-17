package com.example.listi.data.api

import com.example.listi.ui.types.ProductRequest
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ProductListResponse
import com.example.listi.ui.types.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    // Crear un producto
    @POST("api/products")
    fun createProduct(
        @Body body: ProductRequest
    ): Call<Product>

    // Obtener lista de productos con filtros opcionales
    @GET("api/products")
    fun getProducts(
        @Query("name") name: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("sort_by") sortBy: String = "name",
        @Query("order") order: String = "ASC"
    ): Call<ProductListResponse>

    // Obtener un producto por ID
    @GET("api/products/{id}")
    fun getProductById(
        @Path("id") id: Int
    ): Call<Product>

    // Actualizar un producto
    @PUT("api/products/{id}")
    fun updateProduct(
        @Path("id") id: Int,
        @Body body: ProductRequest
    ): Call<ProductResponse>

    // Eliminar un producto
    @DELETE("api/products/{id}")
    fun deleteProduct(
        @Path("id") id: Int
    ): Call<Void>
}
