package com.duck.ducketa.dto

data class OrderFeedbackRegisterDTO(
    val orderId: Long,
    val actualDeliveryTime: Int
)
