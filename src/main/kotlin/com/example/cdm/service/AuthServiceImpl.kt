package com.example.cdm.service

import com.example.cdm.enums.RoleName
import com.example.cdm.model.User
import com.example.cdm.repository.UserRepository
import com.example.cdm.security.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) : AuthService, UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")
        return UserPrincipal(user)
    }

    override fun login(email: String, password: String): String {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        return jwtService.generateToken(UserPrincipal(user))
    }

    override fun register(email: String, password: String): String {
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("User already exists")
        }

        val user = User(
            email = email,
            password = passwordEncoder.encode(password),
            roles = setOf(RoleName.ROLE_USER)
        )

        userRepository.save(user)
        return jwtService.generateToken(UserPrincipal(user))
    }
}
