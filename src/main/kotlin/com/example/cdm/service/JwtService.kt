package com.example.cdm.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration}") private val exp: Long
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(user: UserDetails): String {
        return Jwts.builder()
            .setSubject(user.username)
            .claim("roles", user.authorities.map { it.authority })
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + exp))
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractClaims(token).subject
    }

    fun validateToken(token: String, user: UserDetails): Boolean {
        val claims = extractClaims(token)
        val username = claims.subject
        val expiration = claims.expiration
        return username == user.username && expiration.after(Date())
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
