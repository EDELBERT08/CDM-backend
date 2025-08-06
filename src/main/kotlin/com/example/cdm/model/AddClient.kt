package com.example.cdm.model
import com.example.cdm.dto.toResponseDto

import jakarta.persistence.*
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import com.example.cdm.validation.ValidPatientName
import com.fasterxml.jackson.annotation.JsonCreator
@Entity
@Table(name = "AddClient")
data class AddClient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotNull
    @field:ValidPatientName
    @Column(name = "patient_name", nullable = false)
    var  patientName: String,

    @field:NotNull
    @Column(name = "policy_holder_name", nullable = false)
    val policyHolderName: String,

    @field:NotNull
    @Column(name = "diagnosis", nullable = false)
    var diagnosis: String,



    @Column(name = "doctor_name")
    var doctorName: String? = null,

    @Column(name = "member_number")
    var memberNumber: String? = null,

    @Column(name = "product_type")
    var productType: String? = null,

    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(name = "residence", length = 500)
    var residence: String? = null,

    @Column(name = "email_address")
    var emailAddress: String? = null,

    @Column(name = "services")
    var services: String? = null,


)
