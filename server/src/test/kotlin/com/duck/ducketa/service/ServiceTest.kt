package com.duck.ducketa.service

import com.duck.ducketa.dto.CalculateEtaReqDTO
import com.duck.ducketa.dto.LatLongResponseDTO
import com.duck.ducketa.dto.OrderFeedbackRegisterDTO
import com.duck.ducketa.dto.PredictResponseDTO
import com.duck.ducketa.model.Order
import com.duck.ducketa.repository.OrderFeedbackRepository
import com.duck.ducketa.repository.OrderRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.Month
import java.util.Optional
import kotlin.test.assertEquals

class ServiceTest {
    private val orderRepository = mockk<OrderRepository>()
    private val orderFeedbackRepository = mockk<OrderFeedbackRepository>()
    private val locationService = mockk<LocationService>()
    private val brainService = mockk<BrainService>()

    private val service = Service(orderRepository, orderFeedbackRepository, locationService, brainService)

    @Test
    fun `should calculate ETA and save order`() {
        val request = CalculateEtaReqDTO(
            clientAddress = "Rua Cliente",
            restaurantAddress = "Rua Restaurante",
            orderTime = LocalDateTime.of(2025, Month.JULY, 6, 14, 10, 42),
            queueSize = 2
        )

        val clientLatLon = LatLongResponseDTO("1.0", "2.0")
        val restaurantLatLon = LatLongResponseDTO("3.0", "4.0")
        val predictResponse = PredictResponseDTO(medium = 25, min = 20, max = 35)

        every { locationService.requestLatLon("Rua Cliente") } returns Mono.just(clientLatLon)
        every { locationService.requestLatLon("Rua Restaurante") } returns Mono.just(restaurantLatLon)
        every { brainService.requestPredict(any()) } returns Mono.just(predictResponse)

        every { orderRepository.save(any()) } answers { firstArg() }

        val order = service.calculateEta(request)

        assertEquals(25, order.etaMedium)
        assertEquals(20, order.etaMin)
        assertEquals(35, order.etaMax)
        assertEquals(2, order.queueSize)
        assertEquals(14, order.orderTime.hour)
        assertEquals(1.0, order.clientLocationLat)
        assertEquals(2.0, order.clientLocationLon)
        assertEquals(3.0, order.restaurantLocationLat)
        assertEquals(4.0, order.restaurantLocationLon)

        verify(exactly = 1) { orderRepository.save(any()) }
    }

    @Test
    fun `shound register order feedback`() {
        val order = Order(
            orderTime = LocalDateTime.of(2025, Month.JULY, 6, 14, 10, 42),
            etaMedium = 23,
            etaMax = 25,
            etaMin = 21,
            clientLocationLat = 1.0,
            clientLocationLon = 2.0,
            restaurantLocationLat = 3.0,
            restaurantLocationLon = 4.0,
            queueSize = 2,
            distanceKm = 12.5
        )

        every { orderRepository.findById(1) } returns Optional.of(order)

        every { orderFeedbackRepository.save(any()) } answers { firstArg() }

        val orderFeedback = service.registerOrderFeedback(OrderFeedbackRegisterDTO(1, 22))

        assertEquals(22, orderFeedback.actualDeliveryTime)
        assertEquals(1, orderFeedback.errorMinutes)

        verify(exactly = 1) { orderFeedbackRepository.save(any()) }
    }
}