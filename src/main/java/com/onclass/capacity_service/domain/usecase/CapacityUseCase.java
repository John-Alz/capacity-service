package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.domain.validator.CapacityValidator;
import reactor.core.publisher.Mono;

public class CapacityUseCase implements CapacityServicePort {

    private final CapacityPersistencePort capacityPersistencePort;
    private final TechnologyLinksPort technologyLinksPort;

    public CapacityUseCase(CapacityPersistencePort capacityPersistencePort, TechnologyLinksPort technologyLinksPort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.technologyLinksPort = technologyLinksPort;
    }

    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {
        CapacityValidator.validateForCreate(capacity);
        return capacityPersistencePort.saveCapacity(capacity)
                .doOnNext(saved -> System.out.println("TECHIDS USECASE MS CAP: " + saved.technologyIds()))
                .flatMap(saved ->
                        technologyLinksPort.replaceAll(saved.id(), capacity.technologyIds())
                                .thenReturn(saved));
    }
}
