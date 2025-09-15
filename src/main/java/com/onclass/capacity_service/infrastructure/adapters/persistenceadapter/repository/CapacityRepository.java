package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CapacityRepository extends ReactiveCrudRepository<CapacityEntity, Long> {
}
