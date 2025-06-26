package com.duck.ducketa.controller

import com.duck.ducketa.dto.CalculateEtaReqDTO
import com.duck.ducketa.dto.CalculateEtaResDTO
import com.duck.ducketa.dto.OrderFeedbackRegisterDTO
import com.duck.ducketa.dto.OrderFeedbackResDTO
import com.duck.ducketa.service.Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(val service: Service) {
    @PostMapping("/eta")
    fun calculateEta(@RequestBody request: CalculateEtaReqDTO): ResponseEntity<CalculateEtaResDTO> {
        val orderEta = service.calculateEta(request)

        return ResponseEntity.ok(
            CalculateEtaResDTO(
                mediumTrafficETA = orderEta.etaMediumTraffic,
                lowTrafficETA = orderEta.etaLowTraffic,
                highTrafficETA = orderEta.etaHighTraffic
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
