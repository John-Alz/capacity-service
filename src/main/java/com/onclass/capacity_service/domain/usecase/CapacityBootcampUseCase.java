package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.api.CapacityBootcampServicePort;
import com.onclass.capacity_service.domain.model.BootcampCapacityDetachResult;
import com.onclass.capacity_service.domain.spi.CapacityBootcampPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapacityBootcampUseCase implements CapacityBootcampServicePort {

    private final CapacityBootcampPersistencePort capacityBootcampPersistencePort;

    public CapacityBootcampUseCase(CapacityBootcampPersistencePort capacityBootcampPersistencePort) {
        this.capacityBootcampPersistencePort = capacityBootcampPersistencePort;
    }

    @Override
    public Mono<Void> addAll(Long bootcampId, List<Long> capacityIds) {
        return capacityBootcampPersistencePort.addAll(bootcampId, capacityIds);
    }

    @Override
    public Mono<BootcampCapacityDetachResult> detachLinksByBootcamp(Long bootcampId) {
        return capacityBootcampPersistencePort.detachLinksByBootcamp(bootcampId);
    }
}
