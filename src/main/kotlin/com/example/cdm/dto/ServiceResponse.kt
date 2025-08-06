// Create this in a file like com/example/cdm/dto/ServiceResponse.kt
package com.example.cdm.dto

data class ServiceResponse<T>(
    val status: String,
    val code: Int,
    val message: String,
    val data: T?
)
