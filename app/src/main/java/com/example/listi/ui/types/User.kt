package com.example.listi.ui.types

import java.util.Date

data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val createdAt: Date,
    val updatedAt: Date,
)