// src/main/kotlin/com/example/cdm/config/RoleSeeder.kt
package com.example.cdm.config

import com.example.cdm.enums.RoleName
import com.example.cdm.model.Role
import com.example.cdm.repository.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class RoleSeeder(
    private val roleRepository: RoleRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        RoleName.values().forEach { roleName ->
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(Role(name = roleName))
                println("✅ Seeded role: $roleName")
            }
        }
    }
}
