package com.onclass.capacity_service.domain.api;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.model.CapacityWithTechs;
import com.onclass.capacity_service.domain.model.DeleteBatchResult;
import com.onclass.capacity_service.domain.model.PageResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityServicePort {

    Mono<Capacity> saveCapacity(Capacity capacity);

    Mono<PageResult<CapacityWithTechs>> listItems(int page, int size, boolean ascByName, boolean sortByTechCnt, boolean ascTechCount);
    Mono<List<CapacityWithTechs>> listCapacitiesSummary(Long bootcampId);
    Mono<DeleteBatchResult> deleteByIds(List<Long> ids);


}
