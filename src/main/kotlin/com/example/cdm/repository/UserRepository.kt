package com.example.cdm.repository

import com.example.cdm.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByResetToken(resetToken: String): User?
}
