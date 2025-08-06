package com.example.cdm.dto

import com.example.cdm.model.AddClient
import com.example.cdm.model.CdmProgram
import java.math.BigDecimal

fun AddCdmRequestDto.toEntity(client: AddClient): CdmProgram {
    // Use fallback values to prevent null issues
    val safeProviderCost = providerCost ?: BigDecimal.ZERO
    val safeCurrentCost = currentCost ?: BigDecimal.ZERO

    // Validate cost comparison
    require(safeProviderCost > safeCurrentCost) {
        "Provider cost must be greater than current cost."
    }

    return CdmProgram(
        client = client,
        clientStatus = clientStatus,
        approvalCode = approvalCode,
        lastDateOfRefill = lastDateOfRefill,
        nextDateOfRefill = nextDateOfRefill,
        comments = comments,
        providerReferredFrom = providerReferredFrom,
        providerCost = safeProviderCost,
        providerReferredTo = providerReferredTo,
        currentCost = safeCurrentCost,
        savings = safeProviderCost - safeCurrentCost,
        schemeNameUpdated = schemeNameUpdated
    )
}
