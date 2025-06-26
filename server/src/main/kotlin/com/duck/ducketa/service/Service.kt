package com.duck.ducketa.service

import com.duck.ducketa.dto.CalculateEtaReqDTO
import com.duck.ducketa.dto.OrderFeedbackRegisterDTO
import com.duck.ducketa.model.Order
import com.duck.ducketa.model.OrderFeedback
import com.duck.ducketa.repository.OrderFeedbackRepository
import com.duck.ducketa.repository.OrderRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import kotlin.random.Random

@Service
class Service(
    val orderRepository: OrderRepository,
    val orderFeedbackRepository: OrderFeedbackRepository
) {
    fun calculateEta(request: CalculateEtaReqDTO): Order {
        val order = Order(
            orderTime = request.orderTime,
            etaMediumTraffic = Random.nextInt(10, 20),
            etaHighTraffic = Random.nextInt(20, 30),
            etaLowTraffic = Random.nextInt(5, 10),
            clientLocationLat = -23.583816,
            clientLocationLon = -46.500315,
            restaurantLocationLat = -23.589414,
            restaurantLocationLon = -46.497498,
            queueSize = request.queueSize,
            distanceKm = 20.0
        )

        return orderRepository.save(order)
    }

    fun registerOrderFeedback(request: OrderFeedbackRegisterDTO): OrderFeedback {
        val order = orderRepository
            .findById(request.orderId)
            .orElseThrow { RuntimeException() }

        val orderFeedback = OrderFeedback(
            actualDeliveryTime = request.actualDeliveryTime,
            order = order,
            errorMinutes = order.etaMediumTraffic.compareTo(request.actualDeliveryTime)
        )

        return orderFeedbackRepository.save(orderFeedback)
    }
}