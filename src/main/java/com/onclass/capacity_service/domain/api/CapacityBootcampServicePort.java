package com.onclass.capacity_service.domain.api;

import com.onclass.capacity_service.domain.model.BootcampCapacityDetachResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityBootcampServicePort {

    Mono<Void> addAll(Long bootcampId, List<Long> capacityIds);

    //Contrato de DELETE /capacity-links/bootcamp/{id})
    Mono<BootcampCapacityDetachResult> detachLinksByBootcamp(Long bootcampId);


}
