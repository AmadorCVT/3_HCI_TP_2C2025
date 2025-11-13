package com.example.listi.ui.types

import kotlinx.serialization.Serializable
import java.util.Date

//@Serializable

data class ShoppingList(
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val owner: User,
    val sharedWith: Array<User>,
    val lastPurchasedAt: Date,
    val createdAt: Date,
    val updatedAt: Date,
)

@Serializable
data class UpdateShoppingListRequest(
    val name: String
)
//@Serializable
data class ShoppingListResponse(
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val owner: User,
    val sharedWith: Array<User>,
    val lastPurchasedAt: Date,
    val createdAt: Date,
    val updatedAt: Date,
)

//@Serializable
data class ShoppingListListResponse(
    val data: List<ShoppingList>,
    val pagination: Pagination
)


@Serializable
data class CreateShoppingListRequest(
    val name: String
)


@Serializable
data class ShareShoppingListRequest(
    val userId: Int
)