package com.duck.ducketa.service

import com.duck.ducketa.dto.LatLongResponseDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class LocationService {
    fun requestLatLon(address: String): Mono<LatLongResponseDTO> {
        val client = WebClient.builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .defaultHeader("User-Agent", "DucketaApp/1.0 (kauan@email.com)")
            .build()

        return client.get()
            .uri { uriBuilder ->
                uriBuilder.path("/search")
                    .queryParam("format", "json")
                    .queryParam("q", address)
                    .build()
            }
            .retrieve()
            .bodyToFlux(LatLongResponseDTO::class.java)
            .next()
    }
}