package com.example.cdm.dto

data class UserResponseDto(
    val id: Long,
    val email: String,
    val roles: Set<String>
)
