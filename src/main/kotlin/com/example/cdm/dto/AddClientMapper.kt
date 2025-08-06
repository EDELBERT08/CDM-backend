package com.example.cdm.dto

import com.example.cdm.model.AddClient

fun AddClientRequestDto.toEntity(): AddClient {
    return AddClient(
        patientName = patientName,
        policyHolderName = policyHolderName,
        diagnosis = diagnosis,
        doctorName = doctorName,
        memberNumber = memberNumber,
        productType = productType,
        phoneNumber = phoneNumber,
        residence = residence,
        emailAddress = emailAddress,
        services = services
    )
}

fun AddClient.toResponseDto(): AddClientResponseDto = AddClientResponseDto.from(this)
