package com.example.listi.ui.types

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)