package com.study.febfifteenth.common.utils

import com.study.febfifteenth.common.constants.LogLevel
import com.study.febfifteenth.common.dto.BatchedLogDto
import com.study.febfifteenth.common.dto.LogDto
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks
import java.time.Duration

@Component
class LogAggregator {

    //Unicast; if multi subscribers are required, change it into multicast
    private val logSink = Sinks.many().unicast().onBackpressureBuffer<LogDto>()

    private val logFlux = logSink.asFlux()
        .window(Duration.ofSeconds(2))
        .flatMap { window ->
            window.collectList()
                .filter{ it.isNotEmpty() } // Ignore the empty batch
                .map { logs ->
                    BatchedLogDto(logs)
                }
        }
        .doOnNext { logs -> process(logs) }
        .doOnError { error -> error.printStackTrace() }
//        .onErrorContinue { error, _ -> error.printStackTrace() }

    init {
        logFlux.subscribe()
    }

    fun relayInfo(message: String) {
        logSink.tryEmitNext(LogDto(
            level = LogLevel.INFO,
            message = message
        ))
    }

    // Add Warn, Debug, Error Later

    private fun process(batch: BatchedLogDto) {
        batch.logs.forEach { log ->
            // Demo: This should be written in logger - Non-blocking logging like SLF4J
            println("[${log.timestamp}] ${log.level}: ${log.message}")
        }
    }
}