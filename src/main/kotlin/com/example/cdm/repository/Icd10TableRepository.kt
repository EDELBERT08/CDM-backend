// src/main/kotlin/com/example/cdm/repository/Icd10TableRepository.kt
package com.example.cdm.repository

import com.example.cdm.model.Icd10Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface Icd10TableRepository : JpaRepository<Icd10Table, Long>, JpaSpecificationExecutor<Icd10Table> {
    // You can add custom query methods here if needed,
    // but JpaSpecificationExecutor will handle most filtering.
}