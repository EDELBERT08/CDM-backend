package com.example.cdm.repository

import com.example.cdm.model.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSessionRepository : JpaRepository<UserSession, Long> {
    fun findByUserId(userId: Long): List<UserSession>
}
