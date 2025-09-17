package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity_service.domain.model.BootcampCapacityDetachResult;
import com.onclass.capacity_service.domain.spi.CapacityBootcampPersistencePort;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityBootcampEntity;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CapacityBootcampPersistenceAdapter implements CapacityBootcampPersistencePort {

    private final CapacityBootcampRepository capacityBootcampRepository;
    private final DatabaseClient db;

    @Override
    public Mono<Void> addAll(Long bootcampId, List<Long> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .map(capacityId -> new CapacityBootcampEntity(bootcampId, capacityId))
                .as(capacityBootcampRepository::saveAll)
                .then();
    }

    @Override
    public Mono<BootcampCapacityDetachResult> detachLinksByBootcamp(Long bootcampId) {
        return capacityBootcampRepository.findCapacityIdsByBootcampId(bootcampId)
                .collectList()
                .flatMap(detached -> {
                    Mono<Integer> deleteMono = capacityBootcampRepository.deleteLinksByBootcampId(bootcampId);
                    if (detached.isEmpty()) {
                        return deleteMono.thenReturn(new BootcampCapacityDetachResult(List.of(), List.of()));
                    }
                    List<Long> detachedDistinct = detached.stream().distinct().toList();

                    return deleteMono.then(
                            countLinksByCapacityIds(detachedDistinct)
                                    .map(countMap -> {
                                        List<Long> ophan = detachedDistinct.stream()
                                                .filter(id -> countMap.getOrDefault(id, 0L) == 0)
                                                .toList();
                                        return new BootcampCapacityDetachResult(detachedDistinct, ophan);
                                    })
                    );
                });
    }

    private Mono<Map<Long, Long>> countLinksByCapacityIds(List<Long> capacityIds) {
        if (capacityIds.isEmpty()) return Mono.just(Map.of());

        var distinctIds = capacityIds.stream().distinct().toList();

        return Flux.fromIterable(distinctIds)
                .flatMap(id ->
                        db.sql("SELECT COUNT(*) AS cnt FROM capacity_bootcamp WHERE capacity_id = ?")
                                .bind(0, id)
                                .fetch()
                                .one()
                                .map(m -> Map.entry(id, ((Number) m.get("cnt")).longValue()))
                                .defaultIfEmpty(Map.entry(id, 0L))
                )
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

}
