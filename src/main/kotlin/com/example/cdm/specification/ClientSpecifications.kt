// src/main/kotlin/com/example/cdm/specification/ClientSpecifications.kt

package com.example.cdm.specification

import com.example.cdm.dto.ClientFilterDto
import com.example.cdm.model.AddClient
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root

object ClientSpecifications {
    fun withFilter(filter: ClientFilterDto): Specification<AddClient> {
        return Specification { root: Root<AddClient>, _: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            filter.id?.let {
                predicates += cb.equal(root.get<Long>("id"), it)
            }
            filter.patientName?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("patientName")), "%${it.lowercase()}%")
            }
            filter.policyHolderName?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("policyHolderName")), "%${it.lowercase()}%")
            }
            filter.diagnosis?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("diagnosis")), "%${it.lowercase()}%")
            }
            filter.doctorName?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("doctorName")), "%${it.lowercase()}%")
            }
            filter.memberNumber?.takeIf(String::isNotBlank)?.let {
                predicates += cb.equal(root.get<String>("memberNumber"), it)
            }
            filter.productType?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("productType")), "%${it.lowercase()}%")
            }
            filter.residence?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("residence")), "%${it.lowercase()}%")
            }
            filter.services?.takeIf(String::isNotBlank)?.let {
                predicates += cb.like(cb.lower(root.get("services")), "%${it.lowercase()}%")
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}
