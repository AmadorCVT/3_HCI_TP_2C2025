package com.example.listi.ui.types


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

data class CategoryOwner(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String
)

data class CategoryItem(
    val id: Int,
    val name: String,
    val metadata: Map<String, Any>?,
    val createdAt: String,
    val updatedAt: String,
    val owner: CategoryOwner
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
    val data: List<CategoryItem>,
    val pagination: Pagination
)