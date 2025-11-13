package com.example.listi.ui.types

import java.util.Date
import com.example.listi.ui.types.Product
data class ShoppingListItem(
    val id: Int,
    val unit: String,
    val quantity: Int,
    val purchased: Boolean,
    val lastPurchasedAt: String,
    val createdAt: Date,
    val updatedAt: Date,
    val product: Product
)

data class CreateShoppingListItemRequest(
    val unit: String,
    val quantity: Int,
    val productId: Int
)

data class UpdateShoppingListItemRequest(
    val unit: String,
    val quantity: Int,
    val purchased: Boolean,
    val lastPurchasedAt: String
)

data class ShoppingListItemResponse(
    val id: Int,
    val unit: String,
    val quantity: Int,
    val purchased: Boolean,
    val lastPurchasedAt: String,
    val createdAt: Date,
    val updatedAt: Date,
    val product: Product
)

data class ShoppingListItemListResponse(
    val data: List<ShoppingListItem>,
    val pagination: Pagination

)
