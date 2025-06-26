package com.duck.ducketa.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Order(
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id: Long,
    val clientLocationLat: Double,
    val clientLocationLon: Double,
    val restaurantLocationLat: Double,
    val restaurantLocationLon: Double,
    val orderTime: LocalDateTime,
    val distanceKm: Double,
    val queueSize: Int,
    val etaLowTraffic: Int,
    val etaMediumTraffic: Int,
    val etaHighTraffic: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
