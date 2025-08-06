// src/main/kotlin/com/example/cdm/config/SecurityConfig.kt
package com.example.cdm.config

import com.example.cdm.filter.JwtAuthenticationFilter
import com.example.cdm.security.JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtFilter: JwtAuthenticationFilter,
    private val jwtAuthEntryPoint: JwtAuthenticationEntryPoint
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Disable default login
            .httpBasic { it.disable() }
            .formLogin { it.disable() }

            // Disable CSRF for stateless APIs
            .csrf { it.disable() }

            // Enable CORS
            .cors { it.configurationSource(corsConfigurationSource()) }

            // Exception handling for auth failures
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint(jwtAuthEntryPoint)
            }

            // Authorization rules
            .authorizeHttpRequests { auth ->
                auth
                    // Allow CORS preflight
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    // Public auth endpoints
                    .requestMatchers("/api/auth/**").permitAll()

                    // Public password reset endpoints
                    .requestMatchers(HttpMethod.POST,
                        "/api/users/reset-token",
                        "/api/users/reset-password"
                    ).permitAll()

                    // Swagger/OpenAPI
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                    ).permitAll()

                    // Admin-only user management
                    .requestMatchers("/api/users/**").hasAuthority("ROLE_ADMIN")

                    // All other endpoints authenticated
                    .anyRequest().authenticated()
            }

            // Stateless session
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

            // JWT filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOriginPattern("*")
            addAllowedHeader("*")
            addAllowedMethod("*")
            exposedHeaders = listOf("Authorization")
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

    @Bean
    fun authenticationManager(
        authConfig: AuthenticationConfiguration
    ): AuthenticationManager = authConfig.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
