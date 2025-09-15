package com.onclass.capacity_service.infrastructure.entrypoints.mapper;

import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.model.CapacityWithTechs;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.response.CapacityResponseDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.response.CapacitySummaryResponseDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.response.CapacityWithTechDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapacityMapper {

    Capacity toModel(CapacityRequestDTO capacityRequestDTO);
    CapacityResponseDTO toReponse(Capacity capacity);
    CapacityWithTechDTO toResponseWithTech(CapacityWithTechs capacityWithTechs);
    List<CapacitySummaryResponseDTO> toCapacityResponsesSummary(List<CapacityWithTechs> capacities);

}
