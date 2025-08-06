// src/main/kotlin/com/example/cdm/dto/ClientFilterDto.kt
package com.example.cdm.dto

import java.time.LocalDate

data class ClientFilterDto(
    val id: Long? = null,
    val patientName: String? = null,
    val policyHolderName: String? = null,
    val diagnosis: String? = null,
    val doctorName: String? = null,
    val memberNumber: String? = null,
    val productType: String? = null,
    val residence: String? = null,
    val services: String? = null
)
