package com.example.cdm.dto

import com.example.cdm.model.AddClient

data class AddClientResponseDto(
    val id: Long,
    val patientName: String,
    val policyHolderName: String,
    val diagnosis: String,

    val doctorName: String?,
    val memberNumber: String?,
    val productType: String?,
    val phoneNumber: String?,
    val residence: String?,
    val emailAddress: String?,
    val services: String?
) {
    companion object {
        fun from(client: AddClient): AddClientResponseDto {
            return AddClientResponseDto(
                id = client.id ?: 0L,
                patientName = client.patientName,
                policyHolderName = client.policyHolderName,
                diagnosis = client.diagnosis,
                doctorName = client.doctorName,
                memberNumber = client.memberNumber,
                productType = client.productType,
                phoneNumber = client.phoneNumber,
                residence = client.residence,
                emailAddress = client.emailAddress,
                services = client.services
            )
        }
    }
}
