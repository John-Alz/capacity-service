package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.capacity_service.domain.model.TechnologySummary;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.dto.TechnologySummaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnologyLinksMapper {

    TechnologySummary toModel(TechnologySummaryDTO technologySummaryDTO);
    List<TechnologySummary> toModelList(List<TechnologySummaryDTO> technologySummaryDTOS);

}
