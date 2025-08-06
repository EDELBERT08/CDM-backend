package com.example.cdm.dto
import com.example.cdm.dto.toResponseDto

import com.example.cdm.model.CdmProgram
import com.example.cdm.enums.ClientStatus
import java.time.LocalDate
import java.math.BigDecimal

data class AddCdmResponseDto(
    val id: Long?,
    val clientId: Long,
    val clientStatus: ClientStatus,
    val approvalCode: String?,
    val lastDateOfRefill: LocalDate?,
    val nextDateOfRefill: LocalDate?,
    val comments: String?,
    val schemeNameUpdated: String?,
    val providerReferredFrom: String?,
    val providerCost: BigDecimal?,
    val providerReferredTo: String?,
    val currentCost: BigDecimal?,
    val savings: BigDecimal?,

    // ✅ ADD THIS LINE:
    val client: AddClientDto
)

data class AddClientDto(
    val id: Long?,
    val patientName: String,
    val policyHolderName: String,
    val diagnosis: String,
    val doctorName: String?,
    val phoneNumber: String?,
    val emailAddress: String?,
    val productType: String?,
    val residence: String?
)
fun CdmProgram.toResponseDto(): AddCdmResponseDto {
    return AddCdmResponseDto(
        id = id,
        clientId = client.id!!,
        client = AddClientDto(
            id = client.id,
            patientName = client.patientName,
            policyHolderName = client.policyHolderName,
            diagnosis = client.diagnosis,
            doctorName = client.doctorName,
            phoneNumber = client.phoneNumber,
            emailAddress = client.emailAddress,
            productType = client.productType,
            residence = client.residence
        ),
        clientStatus = clientStatus,
        approvalCode = approvalCode,
        lastDateOfRefill = lastDateOfRefill,
        nextDateOfRefill = nextDateOfRefill,
        comments = comments,
        providerReferredFrom = providerReferredFrom,
        providerCost = providerCost,
        providerReferredTo = providerReferredTo,
        currentCost = currentCost,
        savings = savings,
        schemeNameUpdated = schemeNameUpdated
    )
}
