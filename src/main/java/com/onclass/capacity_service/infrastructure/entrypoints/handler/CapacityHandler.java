package com.onclass.capacity_service.infrastructure.entrypoints.handler;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.exception.ValidationException;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.exception.ExceptionResponse;
import com.onclass.capacity_service.infrastructure.entrypoints.mapper.CapacityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CapacityHandler {

    private final CapacityServicePort capacityServicePort;
    private final CapacityMapper capacityMapper;

    public Mono<ServerResponse> createCapacity(ServerRequest request) {
        return request.bodyToMono(CapacityRequestDTO.class)
                .doOnNext(dto -> System.out.println("🚀 Llega en el request: " + dto))
                .map(capacityMapper::toModel)
                .flatMap(capacityServicePort::saveCapacity)
                .map(capacityMapper::toReponse)
                .flatMap(dto -> ServerResponse
                        .created(URI.create("/capacity/" + dto.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .onErrorResume(ValidationException.class, e -> ServerResponse
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ExceptionResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                e.getMessage()
                        )));
    }

}
