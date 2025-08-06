// src/test/kotlin/com/example/cdm/TestMailConfig.kt
package com.example.cdm

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class TestMailConfig {
    @Bean
    fun javaMailSender(): JavaMailSender = JavaMailSenderImpl()
}
