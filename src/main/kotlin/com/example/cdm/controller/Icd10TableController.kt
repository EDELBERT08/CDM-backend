// src/main/kotlin/com/example/cdm/controller/Icd10TableController.kt
package com.example.cdm.controller

import com.example.cdm.model.Icd10Table
import com.example.cdm.service.Icd10TableService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/icd10")
class Icd10TableController(private val service: Icd10TableService) {

    /**
     * Endpoint to create multiple ICD-10 entries in bulk.
     *
     * @param icd10Entries A list of Icd10Table objects to be saved.
     * @return A list of the created Icd10Table objects with their generated IDs.
     */
    @PostMapping
    fun createBulkIcd10Entries(@Valid @RequestBody icd10Entries: List<Icd10Table>): ResponseEntity<List<Icd10Table>> {
        if (icd10Entries.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val savedEntries = service.saveAll(icd10Entries)
        return ResponseEntity(savedEntries, HttpStatus.CREATED)
    }

    /**
     * Endpoint to retrieve ICD-10 entries. Supports filtering by icd10 code,
     * condition description, and chronic status.
     *
     * @param icd10Code Optional. Filters by ICD-10 code (case-insensitive, partial match).
     * @param conditionDescription Optional. Filters by condition description (case-insensitive, partial match).
     * @param isChronic Optional. Filters by chronic status.
     * @return A list of matching Icd10Table objects.
     */
    @GetMapping
    fun getFilteredIcd10Entries(
        @RequestParam(required = false) icd10Code: String?,
        @RequestParam(required = false) conditionDescription: String?,
        @RequestParam(required = false) isChronic: Boolean?
    ): ResponseEntity<List<Icd10Table>> {
        val entries = service.getAllIcd10Entries(icd10Code, conditionDescription, isChronic)
        return ResponseEntity(entries, HttpStatus.OK)
    }

    /**
     * Endpoint to retrieve a single ICD-10 entry by its ID.
     *
     * @param id The ID of the ICD-10 entry to retrieve.
     * @return The Icd10Table object if found, or 404 Not Found if not found.
     */
    @GetMapping("/{id}")
    fun getIcd10EntryById(@PathVariable id: Long): ResponseEntity<Icd10Table> {
        val entry = service.getIcd10EntryById(id)
        return if (entry != null) {
            ResponseEntity(entry, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}