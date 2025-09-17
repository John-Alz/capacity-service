package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.model.DeleteBatchResult;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {

    private final CapacityRepository capacityRepository;
    private final CapacityEntityMapper capacityEntityMapper;
    private final DatabaseClient db;

    @Override
    public Mono<Capacity> saveCapacity(Capacity capacity) {
        System.out.println("LLega al adapter: " + capacity);
        return capacityRepository.save(capacityEntityMapper.toEntity(capacity))
                .doOnNext(capacity1 -> System.out.println("LLega al adapter v2: " + capacity))
                .map(capacityEntityMapper::toModel);
    }

    @Override
    public Flux<Capacity> findPage(int page, int size, boolean ascByName) {
        int offset = page * size;
        String order = ascByName ? "ASC" : "DESC";

        String sql = """
                SELECT id, name, description
                FROM capacities
                ORDER BY name %s
                LIMIT :limit OFFSET :offset
                """.formatted(order);

        return db.sql(sql)
                .bind("limit", size)
                .bind("offset", offset)
                .map((row, meta) -> {
                    CapacityEntity e = new CapacityEntity();
                    e.setId(row.get("id", Long.class));
                    e.setName(row.get("name", String.class));
                    e.setDescription(row.get("description", String.class));
                    return capacityEntityMapper.toModel(e);
                })
                .all();
    }

    @Override
    public Mono<Long> count() {
        return db.sql("SELECT COUNT(*) AS cnt FROM capacities")
                .map((row, meta) -> row.get("cnt", Long.class))
                .one();
    }

    @Override
    public Flux<Capacity> findByBootcampId(Long bootcampId) {
        return capacityRepository.findBootcampId(bootcampId)
                .map(capacityEntityMapper::toModel);
    }

    @Override
    public Mono<DeleteBatchResult> deleteByIds(List<Long> ids) {
        List<Long> distinct = ids.stream().distinct().toList();
        Set<Long> deleted = new HashSet<>();
        List<Long> notFound = new ArrayList<>();
        return Flux.fromIterable(distinct)
                .flatMap(id ->
                        capacityRepository.existsById(id)
                                .flatMap(exist -> {
                                    if (!exist) {
                                        notFound.add(id);
                                        return Mono.empty();
                                    }
                                    return capacityRepository.deleteById(id)
                                            .then(Mono.fromRunnable(() -> deleted.add(id)));
                                })
                )
                .then(Mono.fromSupplier(() ->
                        new DeleteBatchResult(List.copyOf(deleted), List.copyOf(notFound))));
    }
}
