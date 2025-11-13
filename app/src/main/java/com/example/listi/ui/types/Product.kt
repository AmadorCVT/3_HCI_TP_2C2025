package com.example.listi.ui.types

import java.util.Date
import kotlinx.serialization.Serializable
@Serializable
data class Product(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val category: Category
)
@Serializable
data class CreateProductRequest(
    val name: String,
    val category: CategoryReference
)
@Serializable
data class UpdateProductRequest(
    val name: String,
    val category: CategoryReference,
)

@Serializable
data class CategoryReference(
    val id: Int
)

@Serializable
data class ProductListResponse(
    val data: List<Product>,
    val pagination: Pagination
)