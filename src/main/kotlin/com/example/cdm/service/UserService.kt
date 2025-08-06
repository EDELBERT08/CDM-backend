// src/main/kotlin/com/example/cdm/service/UserService.kt
package com.example.cdm.service

import com.example.cdm.enums.RoleName
import com.example.cdm.exception.NotFoundException
import com.example.cdm.model.User
import com.example.cdm.repository.UserRepository
import com.example.cdm.repository.UserSessionRepository
import com.example.cdm.dto.UserSessionDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

// ─── INTERFACE ─────────────────────────────────────────────────────────────
interface UserService {
    fun adminResetPassword(userId: Long, newPassword: String)
    fun createPasswordResetToken(email: String): String?
    fun resetPassword(token: String, newPassword: String): Boolean
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun findByResetToken(token: String): User?
    fun save(user: User): User
    fun getAllUsers(): List<User>
    fun getSessionsForUser(userId: Long): List<UserSessionDto>
    fun createUser(email: String, password: String, roles: Set<RoleName>): User
}

// ─── IMPLEMENTATION ────────────────────────────────────────────────────────
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createUser(email: String, password: String, roles: Set<RoleName>): User {
        val user = User(
            email = email,
            password = passwordEncoder.encode(password),
            roles = roles
        )
        return userRepository.save(user)
    }

    override fun adminResetPassword(userId: Long, newPassword: String) {
        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("User not found")
        }
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

    override fun createPasswordResetToken(email: String): String {
        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("Email not found")

        val token = UUID.randomUUID().toString()
        user.resetToken = token
        user.resetTokenExpiration = Instant.now().plusSeconds(3600)
        userRepository.save(user)
        return token
    }

    override fun resetPassword(token: String, newPassword: String): Boolean {
        val user = userRepository.findByResetToken(token) ?: return false
        if (user.resetTokenExpiration?.isBefore(Instant.now()) == true) {
            return false
        }
        user.password = passwordEncoder.encode(newPassword)
        user.resetToken = null
        user.resetTokenExpiration = null
        userRepository.save(user)
        return true
    }

    override fun findById(id: Long): User? =
        userRepository.findById(id).orElse(null)

    override fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    override fun findByResetToken(token: String): User? =
        userRepository.findByResetToken(token)

    override fun getAllUsers(): List<User> =
        userRepository.findAll()

    override fun save(user: User): User =
        userRepository.save(user)

    override fun getSessionsForUser(userId: Long): List<UserSessionDto> {
        return userSessionRepository.findByUserId(userId).map {
            UserSessionDto(
                loginTime = it.loginTime,
                logoutTime = it.logoutTime
            )
        }
    }
}

