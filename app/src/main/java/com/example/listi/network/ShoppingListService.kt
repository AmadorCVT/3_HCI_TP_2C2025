package com.example.listi.network

import com.example.listi.ui.types.*
import retrofit2.Response
import retrofit2.http.*

interface ShoppingListService {

    // Crear nueva ShoppingList
    @POST("/api/shopping-lists")
    suspend fun createShoppingList(@Body request: ShoppingListRequest): Response<ShoppingListResponse>

    // Obtener lista de ShoppingList
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
        @Body request: ShoppingListRequest
    ): Response<ShoppingListResponse>

    // Comprar ShoppingList
    @POST("/api/shopping-lists/{id}/purchase")
    suspend fun purchaseShoppingList(
        @Path("id") id: Int,
        @Body request: PurchaseShoppingListRequest
    ): Response<ShoppingList>

    // Resetear ShoppingList
    @POST("/api/shopping-lists/{id}/reset")
    suspend fun resetShoppingList(
        @Path("id") id: Int,
    ): Response<ShoppingList>

    // Obtener usuarios compartidos ShoppingList
    @POST("/api/shopping-lists/{id}/shared-users")
    suspend fun getSharedShoppingList(
        @Path("id") id: Int,
    ): Response<List<User>>

    // Eliminar ShoppingList
    @DELETE("/api/shopping-lists/{id}")
    suspend fun deleteShoppingList(@Path("id") id: Int): Response<Unit>

    @POST("/api/shopping-lists/{id}/share")
    suspend fun shareShoppingList(
        @Path("id") id: Int,
        @Body request: ShareShoppingListRequest
    ): Response<Unit>

    @DELETE("/api/shopping-lists/{id}/share/{user_id}")
    suspend fun removeShareShoppingList(
        @Path("id") id: Int,
        @Path("user_id") userId: Int
    ): Response<Unit>


}