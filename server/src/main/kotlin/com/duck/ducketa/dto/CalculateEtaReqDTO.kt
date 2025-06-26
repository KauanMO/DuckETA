package com.duck.ducketa.dto

import java.time.LocalDateTime

data class CalculateEtaReqDTO(
    val clientAddress: String,
    val restaurantAddress: String,
    val orderTime: LocalDateTime,
    val queueSize: Int
)
