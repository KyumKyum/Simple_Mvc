package com.study.febfifteenth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class FebfifteenthApplication

fun main(args: Array<String>) {
    runApplication<FebfifteenthApplication>(*args)
}
