package com.duck.ducketa.controller

import com.duck.ducketa.dto.*
import com.duck.ducketa.service.Service
import com.duck.ducketa.service.exception.ExceptionModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId

@RestController
class Controller(val service: Service) {
    @Operation(
        summary = "Calcula estimativa de entrega",
        description = "A estimativa é calculada por distância entre endereços e quantidade de pedidos na fila",
        parameters = [
            Parameter(
                name = "clientAddress",
                description = "Endereço do cliente",
                required = true,
                example = "R. Porto Lucena, 43 - SP"
            ),
            Parameter(
                name = "restaurantAddress",
                description = "Endereço do restaurante",
                required = true,
                example = "Av. Aricanduva, 5555 - SP"
            ),
            Parameter(
                name = "queueSize",
                description = "Quantidade de pedidos na fila",
                required = true,
                example = "6"
            ),
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Previsão gerada com sucesso", content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = CalculateEtaResDTO::class))
                ]
            ),
            ApiResponse(
                responseCode = "503",
                description = "Sistema de geração de previsão (brain) fora do ar no momento.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ExceptionModel::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/eta")
    fun calculateEta(
        @RequestParam clientAddress: String,
        @RequestParam restaurantAddress: String,
        @RequestParam queueSize: Int
    )
            : ResponseEntity<CalculateEtaResDTO> {
        val requestDTO = CalculateEtaReqDTO(
            clientAddress,
            restaurantAddress,
            LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
            queueSize
        )

        val orderEta = service.calculateEta(requestDTO)

        return ResponseEntity.ok(
            CalculateEtaResDTO(
                etaMedium = orderEta.etaMedium,
                etaMin = orderEta.etaMin,
                etaMax = orderEta.etaMax
            )
        )
    }

    @Operation(
        summary = "Gera margem de erro do modelo de inteligência artificial",
        description = "A margem de erro do modelo determina o tempo mínimo e máximo de entrega com base em entregas passadas"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Margem de erro gerada com sucesso", content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = EvaluateModelResDTO::class))
                ]
            ),
            ApiResponse(
                responseCode = "503",
                description = "Sistema de geração de previsão (brain) fora do ar no momento.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ExceptionModel::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("evalute-model")
    fun evaluateModel(): ResponseEntity<EvaluateModelResDTO> {
        val evaluateModelRes = service.requestEvaluateModel().block()

        return ResponseEntity.ok(evaluateModelRes)
    }


    @Operation(
        summary = "Cadastra feedback de entrega",
        description = "Cadastra como foi o tempo real de entrega, sendo comparado com o tempo estimado pelo modelo de aprendizado de máquina"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Feedback cadastrado com sucesso", content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = OrderFeedbackResDTO::class))
                ]
            ),
            ApiResponse(
                responseCode = "400", description = "Corpo de requisição inválido"
            )
        ]
    )
    @PostMapping("/feedback")
    fun registerFeedback(@RequestBody @Valid request: OrderFeedbackRegisterDTO): ResponseEntity<OrderFeedbackResDTO> {
        val orderFeedback = service.registerOrderFeedback(request)

        return ResponseEntity.ok(
            OrderFeedbackResDTO(
                id = orderFeedback.id,
                errorMinutes = orderFeedback.errorMinutes,
                actualDeliveryTime = orderFeedback.actualDeliveryTime,
                createdAt = orderFeedback.createdAt
            )
        )
    }
}
