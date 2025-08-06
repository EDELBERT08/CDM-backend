// src/main/kotlin/com/example/cdm/security/JwtAuthenticationEntryPoint.kt
package com.example.cdm.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // send a 401 with a JSON body
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.write("""
            {
              "status": 401,
              "error": "Unauthorized",
              "message": "${authException.message}"
            }
        """.trimIndent())
    }
}
