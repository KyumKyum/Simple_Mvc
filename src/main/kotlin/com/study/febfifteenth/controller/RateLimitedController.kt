package com.study.febfifteenth.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RateLimitedController {
    private val tokenBucket = mutableMapOf<String, Pair<Long, Int>>()
    private val rateLimit = 5
    private val timeWindow = 60_000L // 1min

    @GetMapping("/limited")
    fun accessLimitedEndpoint(@RequestHeader("X-Forwarded-For") clientIp: String): ResponseEntity<String> {
        val curTime = System.currentTimeMillis()
        val (lastTime, tokens) = tokenBucket.getOrDefault(clientIp, Pair(curTime, rateLimit))
        val newToken = if (curTime - lastTime > timeWindow) rateLimit else tokens

        if(newToken > 0){
            tokenBucket[clientIp] = Pair(curTime, newToken - 1) // Get a token
            return ResponseEntity.ok("OK")
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Requests!")

    }
}