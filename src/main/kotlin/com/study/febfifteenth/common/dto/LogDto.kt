package com.study.febfifteenth.common.dto

import com.study.febfifteenth.common.constants.LogLevel
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime



class LogDto (
    val timestamp: LocalDateTime = LocalDateTime.now(),

    @field:NotNull
    val level: LogLevel,

    @field:NotNull
    val message: String,

    val metadata: Map<String, String> = mapOf(),
)

class BatchedLogDto(
    val logs: List<LogDto>,
    val batchedTime: LocalDateTime = LocalDateTime.now(),
)