package com.example.listi.network

import com.example.listi.ui.types.*
import retrofit2.Response
import retrofit2.http.*

interface ShoppingListItemService {

    // Crear nueva ShoppingListItem
    @POST("/api/shopping-lists/{id}/items")
    suspend fun createShoppingListItem(
        @Path("id") id: Int,
        @Body request: ShoppingListItemRequest
    ): Response<ShoppingListItemResponse>

    // Obtener lista de categorías
    @GET("/api/shopping-lists/{id}/items")
    suspend fun getShoppingListItems(
        @Path("id") id: Int,
        @Query("name") name: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("order") order: String = "ASC",
        @Query("sort_by") sortBy: String = "createdAt"
    ): Response<ShoppingListItemListResponse>

    // Obtener una ShoppingListItem por ID
    @GET("/api/shopping-lists/{id}/items/{item_id}")
    suspend fun getShoppingListItemById(@Path("id") id: Int): Response<ShoppingListItemResponse>

    // Actualizar ShoppingListItem
    @PUT("/api/shopping-lists/{id}/items/{item_id}")
    suspend fun updateShoppingListItem(
        @Path("id") id: Int,
        @Body request: UpdateShoppingListItemRequest
    ): Response<ShoppingListItemResponse>

    // Eliminar ShoppingListItem
    @DELETE("/api/shopping-lists/{id}/items/{item_id}")
    suspend fun deleteShoppingListItem(
        @Path("id") id: Int,
        @Path("item_id") itemId: Int): Response<Unit>

    // Actualizar estado de compra de un ShoppingListItem
    @PATCH("/api/shopping-lists/{id}/items/{item_id}")
    suspend fun toggleStatusShoppingListItem(
        @Path("id") id: Int,
        @Path("item_id") itemId: Int,
        @Body body: ToggleShoppingListItemRequest): Response<ShoppingListItem>
}