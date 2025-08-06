// src/main/kotlin/com/example/cdm/dto/CdmFilterDto.kt
package com.example.cdm.dto

import java.math.BigDecimal
import java.time.LocalDate

data class CdmFilterDto(
    val id: Long? = null,
    val clientId: Long? = null,
    val clientStatus: String? = null,
    val approvalCode: String? = null,
    val lastDateOfRefillFrom: LocalDate? = null,
    val lastDateOfRefillTo:   LocalDate? = null,
    val nextDateOfRefillFrom: LocalDate? = null,
    val nextDateOfRefillTo:   LocalDate? = null,
    val comments: String? = null,
    val providerReferredFrom: String? = null,
    val providerCostMin: BigDecimal? = null,
    val providerCostMax: BigDecimal? = null,
    val providerReferredTo: String? = null,
    val savingsMin: BigDecimal? = null,
    val savingsMax: BigDecimal? = null,
    val schemeNameUpdated: String? = null
)
