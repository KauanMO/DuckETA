package com.duck.ducketa.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "app_order")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
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
