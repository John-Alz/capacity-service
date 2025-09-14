package com.onclass.capacity_service.domain.spi;

import com.onclass.capacity_service.domain.model.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityPersistencePort {

    Mono<Capacity> saveCapacity(Capacity capacity);

    Flux<Capacity> findPage(int page, int size, boolean ascByName);
    Mono<Long> count();

}
