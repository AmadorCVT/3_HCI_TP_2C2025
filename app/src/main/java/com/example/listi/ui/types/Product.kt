package com.example.listi.ui.types

import java.util.Date


data class Product(
    val id: Int,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date,
    val category: Category
)
data class CreateProductRequest(
    val name: String,
    val category: CategoryReference,
    val metadata: Map<String, Any> = emptyMap()
)

data class UpdateProductRequest(
    val name: String,
    val category: CategoryReference,
    val metadata: Map<String, Any> = emptyMap()
)


data class CategoryReference(
    val id: Int
)


data class ProductListResponse(
    val data: List<Product>,
    val pagination: Pagination
)