package com.example.listi.ui.types

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class CreateCategoryRequest(
    val name: String
)

@Serializable
data class UpdateCategoryRequest(
    val name: String
)

@Serializable
data class CategoryResponse(
    val id: Int,
    val name: String,
    val updatedAt: String,
    val createdAt: String
)
@Serializable
data class Category(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val metadata: String? = null,
)
@Serializable
data class Pagination(
    val total: Int,
    val page: Int,
    val per_page: Int,
    val total_pages: Int,
    val has_next: Boolean,
    val has_prev: Boolean
)
@Serializable
data class CategoryListResponse(
    val data: List<Category>,
    val pagination: Pagination
)