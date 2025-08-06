package com.example.cdm.model

import jakarta.persistence.*

@Entity
@Table(name = "icd10_table")
data class Icd10Table(

    // Every JPA entity needs a primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "icd10", nullable = false)
    val icd10: String,

    @Column(name = "condition_description", nullable = false)
    val conditionDescription: String,

    @Column(name = "risk_group", nullable = false)
    val riskGroup: String,

    @Column(name = "is_gender_specific", nullable = false)
    val isGenderSpecific: Boolean,

    // This column must be nullable to match the String? type
    @Column(name = "gender")
    val gender: String?,

    @Column(name = "is_chronic", nullable = false)
    val isChronic: Boolean,

    @Column(name = "affected_system", nullable = false)
    val affectedSystem: String
)