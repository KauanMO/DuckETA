package com.duck.ducketa.service.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleBrainOfflineException(exception: BrainOfflineException): ResponseEntity<ExceptionModel> {
        val status = HttpStatus.SERVICE_UNAVAILABLE

        val errorMessage = ExceptionModel(
            status.value(),
            "Serviço de geração de estimativa fora do ar."
        )

        return ResponseEntity(errorMessage, status)
    }

    @ExceptionHandler
    fun handleBrainOfflineException(exception: OrderNotFoundException): ResponseEntity<ExceptionModel> {
        val status = HttpStatus.NOT_FOUND

        val errorMessage = ExceptionModel(
            status.value(),
            "Pedido não encontrado"
        )

        return ResponseEntity(errorMessage, status)
    }
}