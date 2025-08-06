package com.example.cdm.dto


import java.math.BigDecimal

data class AddClientRequestDto(
    val patientName: String,
    val policyHolderName: String,
    val diagnosis: String,

    val doctorName: String,
    val memberNumber: String,
    val productType: String,
    val phoneNumber: String,
    val residence: String,
    val emailAddress: String,
    val services: String,

)
