package com.duck.ducketa.service.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleBrainOfflineException(exception: BrainOfflineException): ResponseEntity<ExceptionModel> {
        val errorMessage = ExceptionModel(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Serviço de geração de estimativa fora do ar."
        )

        return ResponseEntity(errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
    }
}