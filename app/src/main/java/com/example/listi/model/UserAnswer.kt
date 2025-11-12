package com.example.listi.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class UserAnswer(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)