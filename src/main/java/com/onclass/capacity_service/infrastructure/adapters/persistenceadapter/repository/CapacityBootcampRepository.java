package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CapacityBootcampRepository extends ReactiveCrudRepository<CapacityBootcampEntity, Long> {
}
