package com.onclass.capacity_service.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyLinksPort {

    Mono<Void> replaceAll(Long capacityId, List<Long> technologyIds);


}
