package com.example.listi.ui.types

import java.util.Date
import com.example.listi.ui.types.Product
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListItem(
    val id: Int,
    val unit: String,
    val quantity: Int,
    val purchased: Boolean,
    val lastPurchasedAt: String,
    val createdAt: String,
    val updatedAt: String,
    val product: Product
)

@Serializable
data class ShoppingListItemRequest(
    val unit: String,
    val quantity: Int,
    val productId: Int
)

@Serializable
data class UpdateShoppingListItemRequest(
    val unit: String,
    val quantity: Int,
    val purchased: Boolean,
    val lastPurchasedAt: String
)

@Serializable
data class ShoppingListItemResponse(
    val id: Int,
    val unit: String,
    val quantity: Int,
    val purchased: Boolean,
    val lastPurchasedAt: String,
    val createdAt: String,
    val updatedAt: String,
    val product: Product
)

@Serializable
data class ShoppingListItemListResponse(
    val data: List<ShoppingListItem>,
    val pagination: Pagination
)
