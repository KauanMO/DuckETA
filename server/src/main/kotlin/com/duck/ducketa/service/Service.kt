package com.duck.ducketa.service

import com.duck.ducketa.dto.*
import com.duck.ducketa.model.Order
import com.duck.ducketa.model.OrderFeedback
import com.duck.ducketa.repository.OrderFeedbackRepository
import com.duck.ducketa.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.RuntimeException
import kotlin.math.*

@Service
class Service(
    val orderRepository: OrderRepository,
    val orderFeedbackRepository: OrderFeedbackRepository
) {
    fun calculateEta(request: CalculateEtaReqDTO): Order {
        val clientLatLong: LatLongResponseDTO =
            requestLatLon(request.clientAddress).block() ?: throw RuntimeException()
        val restaurantLatLong: LatLongResponseDTO =
            requestLatLon(request.restaurantAddress).block() ?: throw RuntimeException()

        val distanceKm: Double = calculateDistanceKm(
            clientLatLong.lat.toDouble(),
            restaurantLatLong.lat.toDouble(),
            clientLatLong.lon.toDouble(),
            restaurantLatLong.lon.toDouble()
        )

        val predictRequest = PredictRequestDTO(
            distanceKm = distanceKm,
            localTime = request.orderTime.hour,
            queueSize = request.queueSize
        )

        val predict: PredictResponseDTO = requestPredict(predictRequest).block() ?: throw RuntimeException()

        val order = Order(
            orderTime = request.orderTime,
            etaMedium = predict.medium,
            etaMax = predict.max,
            etaMin = predict.min,
            clientLocationLat = clientLatLong.lat.toDouble(),
            clientLocationLon = clientLatLong.lon.toDouble(),
            restaurantLocationLat = restaurantLatLong.lat.toDouble(),
            restaurantLocationLon = restaurantLatLong.lon.toDouble(),
            queueSize = request.queueSize,
            distanceKm = distanceKm
        )

        return orderRepository.save(order)

        return order
    }

    fun requestLatLon(address: String): Mono<LatLongResponseDTO> {
        val client = WebClient.builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .defaultHeader("User-Agent", "DucketaApp/1.0 (kauan@email.com)")
            .build()

        return client.get()
            .uri { uriBuilder ->
                uriBuilder.path("/search")
                    .queryParam("format", "json")
                    .queryParam("q", address)
                    .build()
            }
            .retrieve()
            .bodyToFlux(LatLongResponseDTO::class.java)
            .next()
    }

    fun requestPredict(predictRequest: PredictRequestDTO): Mono<PredictResponseDTO> {
        val client = WebClient.builder()
            .baseUrl("http://localhost:5500")
            .build()

        return client.post()
            .uri("/predict")
            .bodyValue(predictRequest)
            .retrieve()
            .bodyToMono(PredictResponseDTO::class.java)
    }

    fun calculateDistanceKm(lat1: Double, lat2: Double, lon1: Double, lon2: Double): Double {
        val r = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }

    fun registerOrderFeedback(request: OrderFeedbackRegisterDTO): OrderFeedback {
        val order = orderRepository
            .findById(request.orderId)
            .orElseThrow { RuntimeException() }

        val orderFeedback = OrderFeedback(
            actualDeliveryTime = request.actualDeliveryTime,
            order = order,
            errorMinutes = order.etaMedium.compareTo(request.actualDeliveryTime)
        )

        return orderFeedbackRepository.save(orderFeedback)
    }
}