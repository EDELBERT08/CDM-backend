package com.example.cdm.security

import com.example.cdm.enums.RoleName
import com.example.cdm.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        user.roles.map { SimpleGrantedAuthority(it.name) }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getId(): Long? = user.id
    fun getUser(): User = user
}
