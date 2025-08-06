package com.example.cdm.controller

import com.example.cdm.dto.toResponseDto
import com.example.cdm.dto.CdmFilterDto
import com.example.cdm.dto.AddCdmRequestDto
import com.example.cdm.dto.AddCdmResponseDto
import com.example.cdm.dto.toResponseDto
import com.example.cdm.repository.AddClientRepository
import com.example.cdm.repository.CdmProgramRepository
import com.example.cdm.service.CdmProgramService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import com.example.cdm.dto.ClientFilterDto

@RestController
@RequestMapping("/api/cdm")
@CrossOrigin(origins = ["http://localhost:5173"])
class CdmProgramController(
    private val cdmProgramService: CdmProgramService,
    private val clientRepository: AddClientRepository,
    private val cdmRepository: CdmProgramRepository,

) {
    @PostMapping("/filter")
    fun filterCdmPrograms(@RequestBody filter: CdmFilterDto): ResponseEntity<List<AddCdmResponseDto>> {
        val results = cdmProgramService.filterCdmPrograms(filter)
        return ResponseEntity.ok(results)
    }
    @PostMapping("/enroll")
    fun enrollClient(@Valid @RequestBody dto: AddCdmRequestDto): ResponseEntity<AddCdmResponseDto> {
        val response = cdmProgramService.enrollClientInCdm(dto)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/all")
    fun getAllCdmPrograms(): ResponseEntity<List<AddCdmResponseDto>> {
        val allPrograms = cdmRepository.findAll().map { it.toResponseDto() }
        return ResponseEntity.ok(allPrograms)
    }



    @PostMapping("/enroll/bulk")
    fun enrollClientsInBulk(@Valid @RequestBody dtos: List<AddCdmRequestDto>): ResponseEntity<BulkEnrollResult> {
        val successes = mutableListOf<AddCdmResponseDto>()
        val failures = mutableListOf<BulkEnrollFailure>()

        for (dto in dtos) {
            try {
                val result = cdmProgramService.enrollClientInCdm(dto)
                successes.add(result)
            } catch (ex: Exception) {
                failures.add(BulkEnrollFailure(clientId = dto.clientId, error = ex.message ?: "Unknown error"))
            }
        }

        return ResponseEntity.ok(BulkEnrollResult(successes = successes, failures = failures))
    }

    @PutMapping("/{id}")
    fun updateCdmProgram(@PathVariable id: Long, @Valid @RequestBody dto: AddCdmRequestDto): ResponseEntity<AddCdmResponseDto> {
        val existing = cdmRepository.findById(id)
            .orElseThrow { RuntimeException("CDM program not found with id $id") }

        clientRepository.findById(dto.clientId)
            .orElseThrow { RuntimeException("Client not found") }


        val safeProviderCost = dto.providerCost ?: BigDecimal.ZERO
        val safeCurrentCost = dto.currentCost ?: BigDecimal.ZERO
        val calculatedSavings = safeProviderCost.subtract(safeCurrentCost)

        existing.apply {

            this.clientStatus = dto.clientStatus
            this.approvalCode = dto.approvalCode
            this.lastDateOfRefill = dto.lastDateOfRefill
            this.nextDateOfRefill = dto.nextDateOfRefill
            this.comments = dto.comments
            this.providerReferredFrom = dto.providerReferredFrom
            this.providerCost = safeProviderCost
            this.providerReferredTo = dto.providerReferredTo
            this.currentCost = safeCurrentCost
            this.savings = dto.savings ?: calculatedSavings
            this.schemeNameUpdated = dto.schemeNameUpdated
        }

        return ResponseEntity.ok(cdmRepository.save(existing).toResponseDto())
    }
    @GetMapping("/search")
    fun searchCdmProgramByNameOrId(@RequestParam query: String): ResponseEntity<AddCdmResponseDto> {
        val result = cdmProgramService.searchFirstCdmByNameOrId(query)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/search/by-id")
    fun searchCdmProgramById(@RequestParam id: Long): ResponseEntity<AddCdmResponseDto> {
        val result = cdmProgramService.searchFirstCdmById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/search/by-name")
    fun searchCdmProgramByName(@RequestParam name: String): ResponseEntity<AddCdmResponseDto> {
        val result = cdmProgramService.searchFirstCdmByName(name)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(result)
    }
}

data class BulkEnrollResult(
    val successes: List<AddCdmResponseDto>,
    val failures: List<BulkEnrollFailure>
)

data class BulkEnrollFailure(
    val clientId: Long,
    val error: String
)
