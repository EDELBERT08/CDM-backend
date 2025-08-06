// src/main/kotlin/com/example/cdm/model/Role.kt
package com.example.cdm.model

import com.example.cdm.enums.RoleName
import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    val name: RoleName
)
