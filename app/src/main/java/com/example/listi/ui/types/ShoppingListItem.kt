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

