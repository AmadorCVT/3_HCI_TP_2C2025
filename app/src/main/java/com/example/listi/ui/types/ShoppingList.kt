package com.example.listi.ui.types

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.util.Date

//@Serializable

@Serializable
data class ShoppingList(
    val id: Int,
    val name: String,
    val description: String,
    val metadata: String?,
    val recurring: Boolean,
    val owner: User,
    val sharedWith: Array<User>,
    val lastPurchasedAt: String?,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class ShoppingListRequest(
    val name: String,
    val description: String,
    val recurring: Boolean,
    val metadata: JsonElement = JsonObject(emptyMap()),
)
@Serializable
data class ShoppingListResponse(
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val owner: User,
    val sharedWith: Array<User>,
    val lastPurchasedAt: String = "",
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class ShoppingListListResponse(
    val data: List<ShoppingList>,
    val pagination: Pagination
)

@Serializable
data class ShareShoppingListRequest(
    val userMail: String
)
@Serializable
data class PurchaseShoppingListRequest(
    val metadata: String?
)