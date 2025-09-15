package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CapacityRepository extends ReactiveCrudRepository<CapacityEntity, Long> {

    @Query("""
    SELECT c.id, c.name, c.description
    FROM capacities c
    JOIN capacity_bootcamp cb ON cb.capacity_id = c.id
    WHERE cb.bootcamp_id = :bootcampId
    ORDER BY c.name
    """)
    Flux<CapacityEntity> findBootcampId(Long bootcampId);

}
