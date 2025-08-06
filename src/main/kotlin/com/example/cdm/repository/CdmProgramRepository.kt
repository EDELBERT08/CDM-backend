package com.example.cdm.repository

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.example.cdm.model.AddClient
import com.example.cdm.model.CdmProgram
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CdmProgramRepository : JpaRepository<CdmProgram, Long>,
    JpaSpecificationExecutor<CdmProgram> {

    // ✅ Valid if CdmProgram has a ManyToOne relation like: client: AddClient
    fun findAllByClientIdIn(ids: List<Long>): List<CdmProgram>

    // ✅ Works if CdmProgram has: client.patientName: String
    fun findByClient_PatientNameContainingIgnoreCase(name: String): List<CdmProgram>
    fun findFirstByClient(client: AddClient): CdmProgram?

    // ✅ Valid: finds all CDM programs for a client by ID
    fun findByClient_Id(id: Long): List<CdmProgram>
    @Query("""
    SELECT c FROM CdmProgram c 
    WHERE LOWER(c.client.patientName) LIKE LOWER(CONCAT('%', :query, '%')) 
    OR CAST(c.client.id AS string) = :query
""")
    fun findFirstByClientNameOrId(@Param("query") query: String): CdmProgram?

    // ✅ Returns first match by patient name
    fun findFirstByClient_PatientNameContainingIgnoreCase(name: String): CdmProgram?

    // ✅ Good: fetches programs by a full AddClient object
    fun findByClient(client: AddClient): List<CdmProgram>
}
