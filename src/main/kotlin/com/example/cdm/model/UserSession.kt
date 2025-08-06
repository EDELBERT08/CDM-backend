package com.example.cdm.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_sessions")
data class UserSession(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val userId: Long,

    val loginTime: Instant,

    val logoutTime: Instant? = null
)
