package com.example.listi.ui.types

import java.util.Date


data class CreateCategoryRequest(
    val name: String,
    val metadata: Map<String, Any> = emptyMap()
)

data class UpdateCategoryRequest(
    val name: String,
    val metadata: Map<String, Any> = emptyMap()
)

data class CategoryResponse(
    val id: Int,
    val name: String,
    val metadata: Map<String, Any>?,
    val updatedAt: String,
    val createdAt: String
)
data class Category(
    val id: Int,
    val name: String,
    val metadata: Map<String, Any>?,
    val createdAt: Date,
    val updatedAt: Date,
)

data class Pagination(
    val total: Int,
    val page: Int,
    val per_page: Int,
    val total_pages: Int,
    val has_next: Boolean,
    val has_prev: Boolean
)

data class CategoryListResponse(
    val data: List<Category>,
    val pagination: Pagination
)