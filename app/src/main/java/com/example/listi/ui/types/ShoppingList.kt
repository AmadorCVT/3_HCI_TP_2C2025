package com.example.listi.ui.types

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.Date

//@Serializable

@Serializable
data class ShoppingList(
    val id: Int,
    val name: String,
    val description: String,
    val metadata: String,
    val recurring: Boolean,
    val owner: User,
    val sharedWith: Array<User>,
    val lastPurchasedAt: String,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class UpdateShoppingListRequest(
    val name: String,
    val description: String,
    val recurring: Boolean,
)
@Serializable
data class ShoppingListResponse(
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val owner: User,
    val sharedWith: Array<User>,
    val lastPurchasedAt: String,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class ShoppingListListResponse(
    val data: List<ShoppingList>,
    val pagination: Pagination
)


@Serializable
data class CreateShoppingListRequest(
    val name: String,
    val description: String,
    val recurring: Boolean,
)


@Serializable
data class ShareShoppingListRequest(
    val userId: Int
)