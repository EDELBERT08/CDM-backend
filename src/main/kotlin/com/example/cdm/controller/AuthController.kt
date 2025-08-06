package com.example.cdm.controller

import com.example.cdm.dto.AuthResponse
import com.example.cdm.dto.LoginRequest
import com.example.cdm.dto.RegisterRequest
import com.example.cdm.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<AuthResponse> {
        val token = authService.register(req.email, req.password)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> {
        val token = authService.login(req.email, req.password)
        return ResponseEntity.ok(AuthResponse(token))
    }
}

