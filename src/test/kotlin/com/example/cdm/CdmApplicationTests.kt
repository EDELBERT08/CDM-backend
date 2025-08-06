package com.example.cdm

import com.example.cdm.service.AuthService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.userdetails.UserDetailsService

@SpringBootTest(
	properties = [
		"jwt.secret=test-secret-key-1234567890abcdef",
		"jwt.expiration=600000"
	]
)
class CdmApplicationTests {

	@MockBean
	lateinit var javaMailSender: JavaMailSender

	@MockBean
	lateinit var userDetailsService: UserDetailsService

	@MockBean
	lateinit var authService: AuthService

	@Test
	fun contextLoads() {
		// Verifies context startup with mocks in place
	}
}
