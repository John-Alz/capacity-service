package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import reactor.core.publisher.Mono;

public class CapacityUseCase implements CapacityServicePort {

    private final CapacityPersistencePort capacityPersistencePort;

    public CapacityUseCase(CapacityPersistencePort capacityPersistencePort) {
        this.capacityPersistencePort = capacityPersistencePort;
    }

    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {
        return capacityPersistencePort.saveCapacity(capacity);
    }
}
