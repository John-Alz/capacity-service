package com.onclass.capacity_service.infrastructure.entrypoints.handler;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.mapper.CapacityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CapacityHandler {

    private final CapacityServicePort capacityServicePort;
    private final CapacityMapper capacityMapper;

    public Mono<ServerResponse> createCapacity(ServerRequest request) {
        return request.bodyToMono(CapacityRequestDTO.class)
                .map(capacityMapper::toModel)
                .flatMap(capacityServicePort::saveCapacity)
                .map(capacityMapper::toReponse)
                .flatMap(dto -> ServerResponse
                        .created(URI.create("/capacity/" + dto.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

}
