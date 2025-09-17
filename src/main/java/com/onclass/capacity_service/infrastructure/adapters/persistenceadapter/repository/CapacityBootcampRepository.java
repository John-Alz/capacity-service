package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityBootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityBootcampRepository extends ReactiveCrudRepository<CapacityBootcampEntity, Long> {

    @Query("SELECT capacity_id FROM capacity_bootcamp WHERE bootcamp_id = :bootcampId")
    Flux<Long> findCapacityIdsByBootcampId(Long bootcampId);

    @Query("DELETE FROM capacity_bootcamp WHERE bootcamp_id = :bootcampId")
    Mono<Integer> deleteLinksByBootcampId(Long bootcampId);

}
