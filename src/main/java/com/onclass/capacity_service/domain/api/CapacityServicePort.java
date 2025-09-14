package com.onclass.capacity_service.domain.api;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.model.CapacityWithTechs;
import com.onclass.capacity_service.domain.model.PageResult;
import reactor.core.publisher.Mono;

public interface CapacityServicePort {

    Mono<Capacity> saveCapacity(Capacity capacity);

    Mono<PageResult<CapacityWithTechs>> listItems(int page, int size, boolean ascByName, boolean sortByTechCnt, boolean ascTechCount);

}
