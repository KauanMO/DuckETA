package com.duck.ducketa.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class EvaluateModelResDTO(
    @JsonProperty("mean_absolute_error_minutes")
    val meanAbsoluteErrorMinutes: String
)
