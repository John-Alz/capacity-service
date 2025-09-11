package com.onclass.capacity_service.domain.api;

import com.onclass.capacity_service.domain.model.Capacity;
import reactor.core.publisher.Mono;

public interface CapacityServicePort {

    Mono<Capacity> saveCapacity(Capacity capacity);

}
