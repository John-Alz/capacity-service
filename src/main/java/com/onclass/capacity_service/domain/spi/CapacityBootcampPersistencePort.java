package com.onclass.capacity_service.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityBootcampPersistencePort {

    Mono<Void> addAll(Long bootcampId, List<Long> capacityIds);


}
