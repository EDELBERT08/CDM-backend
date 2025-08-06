package com.example.cdm.repository

import com.example.cdm.model.AddClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
interface AddClientRepository : JpaRepository<AddClient, Long>,
    JpaSpecificationExecutor<AddClient> {

    fun findByPolicyHolderNameContainingIgnoreCase(name: String): List<AddClient>

    @Query("SELECT c FROM AddClient c WHERE LOWER(c.patientName) LIKE LOWER(CONCAT('%', :name, '%'))")
    fun findByPatientNameContainingIgnoreCase(@Param("name") name: String): List<AddClient>
    fun findFirstByPatientNameIgnoreCase(name: String): AddClient?


    fun findFirstByPatientNameContainingIgnoreCase(name: String): AddClient?

}
