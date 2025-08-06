package com.example.cdm.controller

import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/api/proxy")
class ProxyController(@Autowired val restTemplate: RestTemplate) {

    @GetMapping
    fun proxy(@RequestParam url: String): ResponseEntity<String> {
        return try {
            val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8)
            val uri = URI(decodedUrl)
            if (!uri.isAbsolute) {
                return ResponseEntity.badRequest().body("Provided URL is not absolute")
            }

            val headers = HttpHeaders()
            headers.accept = listOf(MediaType.APPLICATION_JSON)

            val entity = HttpEntity<String>(headers)
            val response = restTemplate.exchange(
                decodedUrl,
                HttpMethod.GET,
                entity,
                String::class.java
            )

            ResponseEntity.ok(response.body)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Proxy request failed: ${ex.message}")
        }
    }
}
