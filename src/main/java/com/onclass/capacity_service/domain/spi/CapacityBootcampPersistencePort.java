package com.onclass.capacity_service.domain.spi;

import com.onclass.capacity_service.domain.model.BootcampCapacityDetachResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityBootcampPersistencePort {

    Mono<Void> addAll(Long bootcampId, List<Long> capacityIds);

    Mono<BootcampCapacityDetachResult> detachLinksByBootcamp(Long bootcampId);


}
