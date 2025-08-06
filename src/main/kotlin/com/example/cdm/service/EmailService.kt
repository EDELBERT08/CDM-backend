package com.example.cdm.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    fun sendResetEmail(to: String, token: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Password Reset Request"
        message.text = "Click the link to reset your password: https://your-frontend/reset-password?token=$token"
        mailSender.send(message)
    }
}
