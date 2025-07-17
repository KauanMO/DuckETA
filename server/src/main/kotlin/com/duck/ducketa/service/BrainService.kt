package com.duck.ducketa.service

import com.duck.ducketa.dto.EvaluateModelResDTO
import com.duck.ducketa.dto.PredictRequestDTO
import com.duck.ducketa.dto.PredictResponseDTO
import com.duck.ducketa.service.exception.BrainOfflineException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class BrainService(@Value("\${BRAIN_URL}") private val brainUrl: String,) {
    fun requestPredict(predictRequest: PredictRequestDTO): Mono<PredictResponseDTO> {
        val client = WebClient.builder()
            .baseUrl(brainUrl)
            .build()

        return client.post()
            .uri("/predict")
            .bodyValue(predictRequest)
            .retrieve()
            .bodyToMono(PredictResponseDTO::class.java)
            .onErrorMap { BrainOfflineException() }
    }

    fun requestEvaluateModel(): Mono<EvaluateModelResDTO> {
        val client = WebClient.builder()
            .baseUrl(brainUrl)
            .build()

        return client.get()
            .uri("/evaluate-model")
            .retrieve()
            .bodyToMono(EvaluateModelResDTO::class.java)
            .onErrorMap { BrainOfflineException() }
    }
}