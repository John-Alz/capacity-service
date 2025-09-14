package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacityEntityMapper {

    CapacityEntity toEntity(Capacity capacity);
    Capacity toModel(CapacityEntity capacity);

}
