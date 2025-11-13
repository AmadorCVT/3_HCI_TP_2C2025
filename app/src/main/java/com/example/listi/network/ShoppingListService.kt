package com.example.listi.network

import com.example.listi.ui.types.*
import retrofit2.Response
import retrofit2.http.*

interface ShoppingListService {

    // Crear nueva ShoppingList
    @POST("/api/shopping-lists")
    suspend fun createShoppingList(@Body request: CreateShoppingListRequest): Response<ShoppingListResponse>

    // Obtener lista de ShoppingList (con filtros opcionales)
    @GET("/api/shopping-lists")
    suspend fun getShoppingLists(
        @Query("name") name: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("order") order: String = "ASC",
        @Query("sort_by") sortBy: String = "createdAt"
    ): Response<ShoppingListListResponse>

    // Obtener una ShoppingList por ID
    @GET("/api/shopping-lists/{id}")
    suspend fun getShoppingListById(@Path("id") id: Int): Response<ShoppingListResponse>

    // Actualizar ShoppingList
    @PUT("/api/shopping-lists/{id}")
    suspend fun updateShoppingList(
        @Path("id") id: Int,
        @Body request: UpdateShoppingListRequest
    ): Response<ShoppingListResponse>

    // Eliminar ShoppingList
    @DELETE("/api/shopping-lists/{id}")
    suspend fun deleteShoppingList(@Path("id") id: Int): Response<Unit>




}