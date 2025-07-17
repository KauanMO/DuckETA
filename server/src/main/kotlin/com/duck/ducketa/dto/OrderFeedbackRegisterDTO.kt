package com.duck.ducketa.dto

import jakarta.validation.constraints.NotNull

data class OrderFeedbackRegisterDTO(
    @NotNull
    val orderId: Long,
    @NotNull
    val actualDeliveryTime: Int
)
