package com.example.listi.ui.types

import java.util.Date

data class Category(
    val id: Int,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date
)

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: Date,
    val updatedAt: Date,
    val category: Category,
)