// src/main/kotlin/com/example/cdm/repository/RoleRepository.kt
package com.example.cdm.repository

import com.example.cdm.enums.RoleName
import com.example.cdm.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: RoleName): Role?
    fun existsByName(name: RoleName): Boolean
}
