package com.duck.ducketa.dto

import java.time.LocalDateTime

data class OrderFeedbackResDTO(
    val id: Long,
    val actualDeliveryTime: Int,
    val errorMinutes: Int,
    val createdAt: LocalDateTime
)
