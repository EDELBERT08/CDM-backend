package com.example.cdm.service

import com.example.cdm.dto.AddCdmRequestDto
import com.example.cdm.dto.AddCdmResponseDto
import com.example.cdm.dto.toEntity
import com.example.cdm.dto.toResponseDto
import com.example.cdm.model.CdmProgram
import com.example.cdm.model.AddClient
import com.example.cdm.repository.AddClientRepository
import com.example.cdm.repository.CdmProgramRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import com.example.cdm.specification.CdmSpecifications
import com.example.cdm.dto.CdmFilterDto

class ClientNotFoundException(clientId: Long) : RuntimeException("Client not found with ID: $clientId")
class ProgramNotFoundException(programId: Long) : RuntimeException("CDM program not found with ID: $programId")

@Service
class CdmProgramService(
    private val clientRepository: AddClientRepository,
    private val cdmRepository: CdmProgramRepository
) {

    private val logger = LoggerFactory.getLogger(CdmProgramService::class.java)

    @PostConstruct
    fun testCreateTable() {
        if (cdmRepository.count() == 0L) {
            logger.info("Forcing Hibernate to touch CDM table")
        }
    }
    fun filterCdmPrograms(filter: CdmFilterDto): List<AddCdmResponseDto> =
        cdmRepository
            .findAll(CdmSpecifications.withFilter(filter))
            .map { it.toResponseDto() }

    @Transactional
    fun enrollClientInCdm(dto: AddCdmRequestDto): AddCdmResponseDto {
        logger.info("Attempting to enroll client with ID: ${dto.clientId}")

        val client = clientRepository.findById(dto.clientId)
            .orElseThrow { ClientNotFoundException(dto.clientId) }

        val program = dto.toEntity(client)
        val savedProgram = cdmRepository.save(program)

        logger.info("Enrollment successful: Program ID = ${savedProgram.id}")
        return savedProgram.toResponseDto()
    }

    @Transactional
    fun updateCdmProgram(id: Long, dto: AddCdmRequestDto): AddCdmResponseDto {
        logger.info("Updating CDM program with ID: $id")

        val existing = cdmRepository.findById(id)
            .orElseThrow { ProgramNotFoundException(id) }

        val clients = getClientByIdOrName(dto.clientId, dto.patientName)
            ?: throw ClientNotFoundException(dto.clientId)

        val safeProviderCost = dto.providerCost ?: BigDecimal.ZERO
        val safeCurrentCost = dto.currentCost ?: BigDecimal.ZERO
        val calculatedSavings = dto.savings ?: safeProviderCost.subtract(safeCurrentCost)

        existing.apply {
            // this.client = client
            this.clientStatus = dto.clientStatus
            this.approvalCode = dto.approvalCode
            this.lastDateOfRefill = dto.lastDateOfRefill
            this.nextDateOfRefill = dto.nextDateOfRefill
            this.comments = dto.comments
            this.providerReferredFrom = dto.providerReferredFrom
            this.providerCost = safeProviderCost
            this.providerReferredTo = dto.providerReferredTo
            this.currentCost = safeCurrentCost
            this.savings = calculatedSavings
            this.schemeNameUpdated = dto.schemeNameUpdated
        }

        val saved = cdmRepository.save(existing)
        logger.info("CDM update successful for ID: ${saved.id}")
        return saved.toResponseDto()
    }

    fun getClientByIdOrName(id: Long, name: String?): AddClient? {
        return clientRepository.findById(id).orElse(null)
            ?: name?.let {
                cdmRepository.findFirstByClient_PatientNameContainingIgnoreCase(it)?.client
            }
    }

    @Transactional(readOnly = true)
    fun searchFirstCdmById(id: Long): AddCdmResponseDto? {
        val program = cdmRepository.findByClient_Id(id).firstOrNull()
        return program?.toResponseDto()
    }

    fun searchFirstCdmByName(name: String): AddCdmResponseDto? {
        val program = cdmRepository.findFirstByClient_PatientNameContainingIgnoreCase(name)
        return program?.toResponseDto()
    }

    @Transactional(readOnly = true)
    fun searchFirstCdmByNameOrId(query: String): AddCdmResponseDto? {
        val id = query.toLongOrNull()
        val client = id?.let { clientRepository.findById(it).orElse(null) }
            ?: clientRepository.findFirstByPatientNameContainingIgnoreCase(query)

        val program = client?.let { cdmRepository.findFirstByClient(it) }
        return program?.toResponseDto()
    }
}
