package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "capacity_bootcamp")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CapacityBootcampEntity {

    @Id
   private Long id;
   private Long capacityId;
   private Long bootcampId;

    public CapacityBootcampEntity(Long bootcampId, Long capacityId) {
        this.bootcampId = bootcampId;
        this.capacityId = capacityId;
    }
}
