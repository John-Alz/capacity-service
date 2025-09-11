package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {

    private final CapacityRepository capacityRepository;
    private final CapacityEntityMapper capacityEntityMapper;

    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {
        return capacityRepository.save(capacityEntityMapper.toEntity(capacity))
                .map(capacityEntityMapper::toModel);
    }
}
