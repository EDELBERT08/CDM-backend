package com.example.cdm.controller

import com.example.cdm.enums.RoleName
import com.example.cdm.model.User
import com.example.cdm.service.EmailService
import com.example.cdm.service.UserService
import com.example.cdm.dto.UserResponseDto
import com.example.cdm.dto.UserSessionDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val emailService: EmailService
) {
    // ─── CREATE USER ─────────────────────────────────────────────────────────────
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    data class NewUserRequest(
        val email: String,
        val password: String,
        val roles: Set<RoleName> = setOf(RoleName.ROLE_USER)
    )

    @GetMapping("/{userId}/sessions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getUserSessions(@PathVariable userId: Long): ResponseEntity<List<UserSessionDto>> {
        val sessions = userService.getSessionsForUser(userId)
        return ResponseEntity.ok(sessions)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createUser(@RequestBody req: NewUserRequest): ResponseEntity<User> {
        val created = userService.createUser(
            email = req.email.trim().lowercase(),
            password = req.password,
            roles = req.roles
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    // ─── GET ALL USERS ────────────────────────────────────────────────────────────
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponseDto>> {
        val users = userService.getAllUsers().map {
            UserResponseDto(
                id = it.id ?: 0L, // Fallback if id is null
                email = it.email,
                roles = it.roles.map(RoleName::name).toSet()
            )
        }
        return ResponseEntity.ok(users)
    }

    // ─── ADMIN RESET ──────────────────────────────────────────────────────────────
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    data class ResetPasswordDto(val password: String)

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userId}/reset-password")
    fun resetPasswordByAdmin(
        @PathVariable userId: Long,
        @RequestBody req: ResetPasswordDto
    ): ResponseEntity<Void> {
        userService.adminResetPassword(userId, req.password)
        return ResponseEntity.ok().build()
    }

    // ─── SEND RESET TOKEN ─────────────────────────────────────────────────────────
    data class SendResetTokenDto(val email: String)

    @PostMapping("/reset-token")
    fun sendResetToken(@RequestBody req: SendResetTokenDto): ResponseEntity<Void> {
        val token = userService.createPasswordResetToken(req.email)
            ?: return ResponseEntity.badRequest().build()
        emailService.sendResetEmail(req.email, token)
        return ResponseEntity.ok().build()
    }

    // ─── RESET VIA TOKEN ──────────────────────────────────────────────────────────
    data class ResetByTokenDto(val token: String, val newPassword: String)

    @PostMapping("/reset-password")
    fun resetPasswordWithToken(@RequestBody req: ResetByTokenDto): ResponseEntity<Void> {
        val success = userService.resetPassword(req.token, req.newPassword)
        return if (success) ResponseEntity.ok().build()
        else ResponseEntity.badRequest().build()
    }
}
