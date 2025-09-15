package com.onclass.capacity_service.domain.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityBootcampServicePort {

    Mono<Void> addAll(Long bootcampId, List<Long> capacityIds);

}
