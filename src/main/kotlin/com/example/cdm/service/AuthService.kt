package com.example.cdm.service

interface AuthService {

    fun register(email: String, password: String): String
    fun login(email: String, password: String): String

}
