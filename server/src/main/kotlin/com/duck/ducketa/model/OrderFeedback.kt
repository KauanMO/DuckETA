package com.duck.ducketa.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
data class OrderFeedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val actualDeliveryTime: Int,
    val errorMinutes: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    val order: Order
)
