package com.example.cdm.model

import jakarta.persistence.*
import java.time.Instant
import com.example.cdm.enums.RoleName

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    var password: String,

    var resetToken: String? = null,
    var resetTokenExpiration: Instant? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Enumerated(EnumType.STRING) // ✅ Store enum as String
    @Column(name = "role")
    val roles: Set<RoleName> = setOf(RoleName.ROLE_USER, RoleName.ROLE_ADMIN)
)
