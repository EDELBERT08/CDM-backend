// src/main/kotlin/com/example/cdm/specification/CdmSpecifications.kt
package com.example.cdm.specification

import com.example.cdm.dto.CdmFilterDto
import com.example.cdm.model.CdmProgram
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root

object CdmSpecifications {
    fun withFilter(filter: CdmFilterDto): Specification<CdmProgram> {
        return Specification { root: Root<CdmProgram>, _: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val p = mutableListOf<Predicate>()

            filter.id?.let {
                p += cb.equal(root.get<Long>("id"), it)
            }
            filter.clientId?.let {
                p += cb.equal(root.get<Any>("client").get<Long>("id"), it)
            }
            filter.clientStatus?.takeIf(String::isNotBlank)?.let {
                p += cb.equal(cb.lower(root.get("clientStatus")), it.lowercase())
            }
            filter.approvalCode?.takeIf(String::isNotBlank)?.let {
                p += cb.like(cb.lower(root.get("approvalCode")), "%${it.lowercase()}%")
            }
            filter.lastDateOfRefillFrom?.let {
                p += cb.greaterThanOrEqualTo(root.get("lastDateOfRefill"), it)
            }
            filter.lastDateOfRefillTo?.let {
                p += cb.lessThanOrEqualTo(root.get("lastDateOfRefill"), it)
            }
            filter.nextDateOfRefillFrom?.let {
                p += cb.greaterThanOrEqualTo(root.get("nextDateOfRefill"), it)
            }
            filter.nextDateOfRefillTo?.let {
                p += cb.lessThanOrEqualTo(root.get("nextDateOfRefill"), it)
            }
            filter.comments?.takeIf(String::isNotBlank)?.let {
                p += cb.like(cb.lower(root.get("comments")), "%${it.lowercase()}%")
            }
            filter.providerReferredFrom?.takeIf(String::isNotBlank)?.let {
                p += cb.like(cb.lower(root.get("providerReferredFrom")), "%${it.lowercase()}%")
            }
            filter.providerCostMin?.let {
                p += cb.ge(root.get("providerCost"), it)
            }
            filter.providerCostMax?.let {
                p += cb.le(root.get("providerCost"), it)
            }
            filter.providerReferredTo?.takeIf(String::isNotBlank)?.let {
                p += cb.like(cb.lower(root.get("providerReferredTo")), "%${it.lowercase()}%")
            }
            filter.savingsMin?.let {
                p += cb.ge(root.get("savings"), it)
            }
            filter.savingsMax?.let {
                p += cb.le(root.get("savings"), it)
            }
            filter.schemeNameUpdated?.takeIf(String::isNotBlank)?.let {
                p += cb.like(cb.lower(root.get("schemeNameUpdated")), "%${it.lowercase()}%")
            }

            cb.and(*p.toTypedArray())
        }
    }
}
