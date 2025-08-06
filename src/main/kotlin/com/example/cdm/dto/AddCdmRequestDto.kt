package com.example.cdm.dto

import com.example.cdm.model.AddClient
import com.example.cdm.model.CdmProgram
import com.example.cdm.enums.ClientStatus

import java.math.BigDecimal
import java.time.LocalDate

data class AddCdmRequestDto(
    val clientId: Long,
    val clientStatus: ClientStatus,
    val approvalCode: String?,
    val lastDateOfRefill: LocalDate?,
    val nextDateOfRefill: LocalDate?,
    val comments: String?,
    val providerReferredFrom: String?,
    val providerCost: BigDecimal?,
    val providerReferredTo: String?,
    val currentCost: BigDecimal?,
    val savings: BigDecimal?,
    val schemeNameUpdated: String?,
    val patientName: String? = null // ✅ Add this if it's missing
)
