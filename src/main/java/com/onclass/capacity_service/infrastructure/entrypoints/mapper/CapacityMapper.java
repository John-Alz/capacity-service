package com.onclass.capacity_service.infrastructure.entrypoints.mapper;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.response.CapacityResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapacityMapper {

    Capacity toModel(CapacityRequestDTO capacityRequestDTO);
    CapacityResponseDTO toReponse(Capacity capacity);

}
