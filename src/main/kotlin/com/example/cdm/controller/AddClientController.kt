package com.example.cdm.controller

import com.example.cdm.dto.AddClientRequestDto
import com.example.cdm.dto.AddClientResponseDto
import com.example.cdm.dto.ServiceResponse
import com.example.cdm.dto.toEntity
import com.example.cdm.dto.toResponseDto
import com.example.cdm.service.AddClientService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.cdm.dto.ClientFilterDto

@CrossOrigin(origins = ["http://localhost:5173"]) // Adjust this for your Vue dev server
@RestController
@RequestMapping("/api/clients")
class AddClientController(
    private val clientService: AddClientService
) {

    @PostMapping
    fun createClient(@Valid @RequestBody dto: AddClientRequestDto): ResponseEntity<ServiceResponse<AddClientResponseDto>> {
        val response = clientService.addClient(dto.toEntity())
        val mappedResponse = ServiceResponse(
            status = response.status,
            code = response.code,
            message = response.message,
            data = response.data?.toResponseDto()
        )
        return ResponseEntity.status(mappedResponse.code).body(mappedResponse)
    }
    @PostMapping("/filter")
    fun filterClients(@RequestBody filter: ClientFilterDto): ResponseEntity<List<AddClientResponseDto>> {
        val results = clientService.filterClients(filter)
        return ResponseEntity.ok(results)
    }
    @PostMapping("/bulk")
    fun createClients(@RequestBody clients: List<AddClientRequestDto>): ResponseEntity<List<ServiceResponse<AddClientResponseDto>>> {
        val responses = clients.map { dto ->
            val result = clientService.addClient(dto.toEntity())
            ServiceResponse(
                status = result.status,
                code = result.code,
                message = result.message,
                data = result.data?.toResponseDto()
            )
        }
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/search")
    fun searchFirstByName(@RequestParam name: String): ResponseEntity<AddClientResponseDto> {
        val client = clientService.getClientByName(name)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(client.toResponseDto())
    }

    @PutMapping("/{id}")
    fun updateClient(
        @PathVariable id: Long,
        @Valid @RequestBody dto: AddClientRequestDto
    ): ResponseEntity<AddClientResponseDto> {

        val client = clientService.getClientById(id)
            ?: dto.patientName.takeIf { it.isNotBlank() }
                ?.let { clientService.getClientByName(it) }
            ?: return ResponseEntity.notFound().build()

        val updatedClient = clientService.updateClient(client.id!!, dto.toEntity())
        return ResponseEntity.ok(updatedClient.toResponseDto())
    }

    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Long): ResponseEntity<AddClientResponseDto> {
        val client = clientService.getClientById(id)?.toResponseDto()
        return client?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllClients(): ResponseEntity<List<AddClientResponseDto>> {
        val clients = clientService.getAllClients().map { it.toResponseDto() }
        return ResponseEntity.ok(clients)
    }

    @GetMapping("/find")
    fun findClientByIdOrName(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?
    ): ResponseEntity<AddClientResponseDto> {
        val client = clientService.getClientByIdOrName(id, name)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(client.toResponseDto())
    }

    @GetMapping("/search/name")
    fun searchByName(@RequestParam name: String): ResponseEntity<Any> {
        if (name.isBlank() || name.length < 2) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Name must be at least 2 characters"))
        }

        val clients = clientService.searchByName(name).map { it.toResponseDto() }
        return ResponseEntity.ok(clients)
    }

    @GetMapping("/search/policy-holder")
    fun searchByPolicyHolder(@RequestParam name: String): ResponseEntity<List<AddClientResponseDto>> {
        val clients = clientService.searchByPolicyHolderName(name).map { it.toResponseDto() }
        return ResponseEntity.ok(clients)
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Long): ResponseEntity<Void> {
        return if (clientService.deleteClient(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
