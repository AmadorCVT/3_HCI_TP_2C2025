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
    val lastPurchasedAt: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val product: Product
)

@Serializable
data class ShoppingListItemRequest(
    val unit: String,
    val quantity: Int,
    val product: ProductReference
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
    val item: ShoppingListItem
)

@Serializable
data class ShoppingListItemListResponse(
    val data: List<ShoppingListItem>,
    val pagination: Pagination
)
