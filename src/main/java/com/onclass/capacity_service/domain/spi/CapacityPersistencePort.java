package com.onclass.capacity_service.domain.spi;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.model.DeleteBatchResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityPersistencePort {

    Mono<Capacity> saveCapacity(Capacity capacity);

    Flux<Capacity> findPage(int page, int size, boolean ascByName);
    Mono<Long> count();

    Flux<Capacity> findByBootcampId(Long bootcampId);
    Mono<DeleteBatchResult> deleteByIds(List<Long> ids);


}
