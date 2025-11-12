package com.example.listi.model

import com.example.listi.ui.types.User
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ShoppingLIstsAnswer (
    @SerialName(value = "image")
    val id: Int,
    val name: String,
    val description: String,
    val recurring: Boolean,
    val owner: LocalDateTime,
    val sharedWith: Array<UserAnswer>,
    val lastPurchasedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)