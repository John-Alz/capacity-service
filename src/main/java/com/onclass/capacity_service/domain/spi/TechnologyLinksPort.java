package com.onclass.capacity_service.domain.spi;

import com.onclass.capacity_service.domain.model.TechnologySummary;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyLinksPort {

    Mono<Void> replaceAll(Long capacityId, List<Long> technologyIds);
    Mono<List<TechnologySummary>> listByCapacityId(Long capacityId);

}
