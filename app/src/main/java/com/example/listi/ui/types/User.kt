package com.example.listi.ui.types

import java.util.Date

data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)