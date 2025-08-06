// src/main/kotlin/com/example/cdm/service/Icd10TableService.kt
package com.example.cdm.service

import com.example.cdm.model.Icd10Table
import com.example.cdm.repository.Icd10TableRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Icd10TableService(private val repository: Icd10TableRepository) {

    @Transactional
    fun saveAll(icd10Entries: List<Icd10Table>): List<Icd10Table> {
        return repository.saveAll(icd10Entries)
    }

    fun getAllIcd10Entries(
        icd10Code: String?,
        conditionDescription: String?,
        isChronic: Boolean?
    ): List<Icd10Table> {
        val spec = Icd10Specification(icd10Code, conditionDescription, isChronic)
        return repository.findAll(spec)
    }

    fun getIcd10EntryById(id: Long): Icd10Table? {
        return repository.findById(id).orElse(null)
    }
}