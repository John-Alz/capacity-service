package com.onclass.capacity_service.infrastructure.entrypoints.handler;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.exception.ValidationException;
import com.onclass.capacity_service.domain.model.PageResult;
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
import java.util.Arrays;
import java.util.List;

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

    public Mono<ServerResponse> listCapacity(ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = request.queryParam("size").map(Integer::parseInt).orElse(10);
        String sortBy = request.queryParam("sortBy").orElse("name");
        String order = request.queryParam("order").orElse("asc");

        boolean ascByName = !"desc".equalsIgnoreCase(order) && !"technologyCount".equalsIgnoreCase(sortBy);
        boolean sortByTechCnt = "technologyCount".equalsIgnoreCase(sortBy);
        boolean ascTechCount = !"desc".equalsIgnoreCase(order);

        return capacityServicePort.listItems(page, size, ascByName, sortByTechCnt, ascTechCount)
                .map(pageResult -> new PageResult<>(
                        pageResult.page(),
                        pageResult.size(),
                        pageResult.total(),
                        pageResult.items().stream().map(capacityMapper::toResponseWithTech).toList()
                ))
                .flatMap(body -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body))
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

    public Mono<ServerResponse> listCapacitiesSummary(ServerRequest request) {
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));
        return capacityServicePort.listCapacitiesSummary(bootcampId)
                .map(capacityMapper::toCapacityResponsesSummary)
                .flatMap(dtos -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dtos));

    }

    public Mono<ServerResponse> deleteByIds(ServerRequest request) {
        List<Long> ids = request.queryParam("ids")
                .map(s -> Arrays.stream(s.split(","))
                        .filter(p -> !p.isBlank())
                        .map(Long::valueOf)
                        .toList())
                .orElse(List.of());
        return capacityServicePort.deleteByIds(ids)
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res));
    }
}
