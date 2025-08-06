// src/main/kotlin/com/example/cdm/service/Icd10Specification.kt
package com.example.cdm.service

import com.example.cdm.model.Icd10Table
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class Icd10Specification(
    private val icd10Code: String?,
    private val conditionDescription: String?,
    private val isChronic: Boolean?
) : Specification<Icd10Table> {

    override fun toPredicate(
        root: Root<Icd10Table>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()

        icd10Code?.let {
            // Explicitly get the path as a String
            val path = root.get<String>("icd10")
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%${it.lowercase()}%"))
        }

        conditionDescription?.let {
            // Explicitly get the path as a String
            val path = root.get<String>("conditionDescription")
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%${it.lowercase()}%"))
        }

        isChronic?.let {
            // Explicitly get the path as a Boolean to resolve the error
            val path = root.get<Boolean>("isChronic")
            predicates.add(criteriaBuilder.equal(path, it))
        }

        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}