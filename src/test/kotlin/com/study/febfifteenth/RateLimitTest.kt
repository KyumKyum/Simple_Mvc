package com.study.febfifteenth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RateLimitTest{
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `test rate-limit - under limit`(){
        val headers = HttpHeaders()
        headers.set("X-Forwarded-For", "192.168.1.1")
        val entity = HttpEntity<String>(headers)
        for (i in 1..5){
            val resp = restTemplate.exchange("/api/limited", HttpMethod.GET, entity, String::class.java)
            assertEquals(HttpStatus.OK, resp.statusCode)
            assertEquals("OK", resp.body)
        }
    }

    @Test
    fun `test rate-limit - exceed limit`(){
        val headers = HttpHeaders()
        headers.set("X-Forwarded-For", "192.168.1.1")
        val entity = HttpEntity<String>(headers)
        for (i in 1..5){
            restTemplate.exchange("/api/limited", HttpMethod.GET, entity, String::class.java)
        }

        val resp =  restTemplate.exchange("/api/limited", HttpMethod.GET, entity, String::class.java)
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, resp.statusCode)
        assertEquals("Too Many Requests!", resp.body)
    }
}