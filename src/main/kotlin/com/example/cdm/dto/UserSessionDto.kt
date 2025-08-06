package com.example.cdm.dto

import java.time.Instant

data class UserSessionDto(
    val loginTime: Instant,
    val logoutTime: Instant?
)
