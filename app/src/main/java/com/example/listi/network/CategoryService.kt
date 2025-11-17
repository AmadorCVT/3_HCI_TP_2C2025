package com.example.listi.network

import com.example.listi.ui.types.*
import retrofit2.Response
import retrofit2.http.*

interface CategoryService {

    // Crear nueva categoría
    @POST("/api/categories")
    suspend fun createCategory(@Body request: CreateCategoryRequest): Response<CategoryResponse>

    // Obtener lista de categorías
    @GET("/api/categories")
    suspend fun getCategories(
        @Query("name") name: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("order") order: String = "ASC",
        @Query("sort_by") sortBy: String = "createdAt"
    ): Response<CategoryListResponse>

    // Obtener una categoría por ID
    @GET("/api/categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<CategoryResponse>

    // Actualizar categoría
    @PUT("/api/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body request: UpdateCategoryRequest
    ): Response<CategoryResponse>

    // Eliminar categoría
    @DELETE("/api/categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<Unit>
}
