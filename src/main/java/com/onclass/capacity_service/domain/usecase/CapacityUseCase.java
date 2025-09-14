package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.exception.ValidationException;
import com.onclass.capacity_service.domain.model.*;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.domain.util.DomainConstants;
import com.onclass.capacity_service.domain.validator.CapacityValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

public class CapacityUseCase implements CapacityServicePort {

    private final CapacityPersistencePort capacityPersistencePort;
    private final TechnologyLinksPort technologyLinksPort;

    public CapacityUseCase(CapacityPersistencePort capacityPersistencePort, TechnologyLinksPort technologyLinksPort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.technologyLinksPort = technologyLinksPort;
    }

    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {
        CapacityValidator.validateForCreate(capacity);
        return capacityPersistencePort.saveCapacity(capacity)
                .doOnNext(saved -> System.out.println("TECHIDS USECASE MS CAP: " + saved.technologyIds()))
                .flatMap(saved ->
                        technologyLinksPort.replaceAll(saved.id(), capacity.technologyIds())
                                .thenReturn(saved));
    }


    @Override
    public Mono<PageResult<CapacityWithTechs>> listItems(int page, int size, boolean ascByName, boolean sortByTechCnt, boolean ascTechCount) {
        if (page < DomainConstants.MIN_LENGTH_PAGE)  return Mono.error(new ValidationException(DomainConstants.MIN_LENGTH_PAGE_MESSAGE));
        if (size <= DomainConstants.MIN_LENGTH_SIZE) return Mono.error(new ValidationException(DomainConstants.MIN_LENGTH_SIZE_MESSAGE));
        Mono<List<Capacity>> items = capacityPersistencePort.findPage(page, size, ascByName).collectList();
        Mono<Long> total  = capacityPersistencePort.count();
        return items.flatMap(list ->
                Flux.fromIterable(list)
                        .concatMap(cap ->
                                technologyLinksPort.listByCapacityId(cap.id())
                                        .defaultIfEmpty(List.of())
                                        .map(techs -> new CapacityWithTechs(
                                                cap.id(), cap.name(), cap.description(), techs))
                        )
                        .collectList()
                        .map(result -> {
                            if (sortByTechCnt) {
                                var cmp = Comparator.comparingInt((CapacityWithTechs c) -> c.technologies().size());
                                if (!ascTechCount) cmp = cmp.reversed();
                                result.sort(cmp);
                            } else {
                                var cmp = Comparator.comparing(CapacityWithTechs::name, String.CASE_INSENSITIVE_ORDER);
                                if (!ascByName) cmp = cmp.reversed();
                                result.sort(cmp);
                            }
                            return result;
                        })
                        .zipWith(total, (withTech, tot) ->
                                new PageResult<>(page, size, tot, withTech))
        );
    }
}
