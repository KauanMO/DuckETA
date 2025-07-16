package com.duck.ducketa.controller

import com.duck.ducketa.dto.CalculateEtaReqDTO
import com.duck.ducketa.dto.CalculateEtaResDTO
import com.duck.ducketa.dto.OrderFeedbackRegisterDTO
import com.duck.ducketa.dto.OrderFeedbackResDTO
import com.duck.ducketa.service.Service
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
    @GetMapping("/eta")
    fun calculateEta(
        @RequestParam clientAddress: String,
        @RequestParam restaurantAddress: String,
        @RequestParam queueSize: Int
    )
            : ResponseEntity<CalculateEtaResDTO> {
        val requestDTO = CalculateEtaReqDTO(clientAddress, restaurantAddress, LocalDateTime.now(ZoneId.of("America/Sao_Paulo")), queueSize)

        val orderEta = service.calculateEta(requestDTO)

        return ResponseEntity.ok(
            CalculateEtaResDTO(
                etaMedium = orderEta.etaMedium,
                etaMin = orderEta.etaMin,
                etaMax = orderEta.etaMax
            )
        )
    }

    @PostMapping("/feedback")
    fun registerFeedback(@RequestBody request: OrderFeedbackRegisterDTO): ResponseEntity<OrderFeedbackResDTO> {
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
