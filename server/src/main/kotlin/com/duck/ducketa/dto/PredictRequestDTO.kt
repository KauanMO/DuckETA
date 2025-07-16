package com.duck.ducketa.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PredictRequestDTO(
    @JsonProperty("distance_km")
    val distanceKm: Double,
    @JsonProperty("local_time")
    val localTime: Int,
    @JsonProperty("queue_size")
    val queueSize: Int,
)
