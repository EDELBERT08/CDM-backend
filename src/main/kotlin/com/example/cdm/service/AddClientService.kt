package com.example.cdm.service

import com.example.cdm.model.AddClient
import com.example.cdm.dto.ServiceResponse
import com.example.cdm.dto.AddClientResponseDto
import com.example.cdm.repository.AddClientRepository
import org.springframework.stereotype.Service
import com.example.cdm.specification.ClientSpecifications
import com.example.cdm.dto.ClientFilterDto
import com.example.cdm.dto.toResponseDto

@Service
class AddClientService(private val repository: AddClientRepository) {
    fun addClient(client: AddClient): ServiceResponse<AddClient> {
        return try {
            val savedClient = repository.save(client)
            ServiceResponse(
                status = "success",
                code = 200,
                message = "Client added successfully",
                data = savedClient
            )
        } catch (ex: Exception) {
            ServiceResponse(
                status = "error",
                code = 500,
                message = "Failed to add client: ${ex.localizedMessage}",
                data = null
            )
        }
    }
    fun filterClients(filter: ClientFilterDto): List<AddClientResponseDto> =
        repository
            .findAll(ClientSpecifications.withFilter(filter))
            .map { it.toResponseDto() }

    fun getClientByIdOrName(id: Long?, name: String?): AddClient? {
        return when {
            id != null -> getClientById(id)
            !name.isNullOrBlank() -> getClientByName(name)
            else -> null
        }
    }

    fun getClientById(id: Long): AddClient? {
        return repository.findById(id).orElse(null)
    }

    fun getClientByName(name: String): AddClient? {
        return repository.findFirstByPatientNameIgnoreCase(name)
    }

    fun updateClient(id: Long, updated: AddClient): AddClient {
        val existing = repository.findById(id).orElseThrow {
            IllegalArgumentException("Client with ID $id not found")
        }

        val updatedClient = updated.copy(id = existing.id)
        return repository.save(updatedClient)
    }
    fun deleteClient(id: Long): Boolean {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun getAllClients(): List<AddClient> {
        return repository.findAll()
    }


    fun searchByName(name: String): List<AddClient> {
        return repository.findByPatientNameContainingIgnoreCase(name)
    }


    fun searchByPolicyHolderName(name: String): List<AddClient> {
        return repository.findByPolicyHolderNameContainingIgnoreCase(name)
    }
}
