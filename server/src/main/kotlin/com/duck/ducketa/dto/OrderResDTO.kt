package com.duck.ducketa.dto

import com.duck.ducketa.model.Order
import java.time.LocalDateTime

data class OrderResDTO(
    val id: Long,
    val clientLocationLat: Double,
    val clientLocationLon: Double,
    val restaurantLocationLat: Double,
    val restaurantLocationLon: Double,
    val orderTime: LocalDateTime,
    val distanceKm: Double,
    val queueSize: Int,
    val etaMin: Int,
    val etaMedium: Int,
    val etaMax: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(order: Order) : this(
        id = order.id,
        clientLocationLat = order.clientLocationLat,
        clientLocationLon = order.clientLocationLon,
        restaurantLocationLat = order.restaurantLocationLat,
        restaurantLocationLon = order.restaurantLocationLon,
        orderTime = order.orderTime,
        distanceKm = order.distanceKm,
        queueSize = order.queueSize,
        etaMin = order.etaMin,
        etaMedium = order.etaMedium,
        etaMax = order.etaMax,
        createdAt = order.createdAt
    )
}