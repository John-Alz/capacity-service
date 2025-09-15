package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity_service.domain.spi.CapacityBootcampPersistencePort;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityBootcampEntity;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityBootcampRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CapacityBootcampPersistenceAdapter implements CapacityBootcampPersistencePort {

    private final CapacityBootcampRepository capacityBootcampRepository;

    @Override
    public Mono<Void> addAll(Long bootcampId, List<Long> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .map(capacityId -> new CapacityBootcampEntity(bootcampId, capacityId))
                .as(capacityBootcampRepository::saveAll)
                .then();
    }
}
