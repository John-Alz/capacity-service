package com.onclass.capacity_service.infrastructure.entrypoints.handler;

import com.onclass.capacity_service.domain.api.CapacityBootcampServicePort;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityBootcampRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.response.SuccessReponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CapacityBootcampHandler {

    private final CapacityBootcampServicePort capacityBootcampServicePort;

    public Mono<ServerResponse> saveCapacityBootcamp(ServerRequest request) {
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));
        return request.bodyToMono(CapacityBootcampRequestDTO.class)
                .flatMap(dto -> capacityBootcampServicePort.addAll(bootcampId, dto.capacitiesId()))
                .then(Mono.defer(() -> ServerResponse
                        .created(URI.create("/capacity-bootcamps/" + bootcampId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new SuccessReponseDTO("Relacion Capacity Bootcamp Creado.", LocalDateTime.now()))));
    }

    public Mono<ServerResponse> detachByBootcamp(ServerRequest request) {
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));
        return capacityBootcampServicePort.detachLinksByBootcamp(bootcampId)
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(result));
    }

}
