package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "capacities")
@Getter
@Setter
@RequiredArgsConstructor
public class CapacityEntity {

    @Id
    private Long id;
    private String name;
    private String description;

}
