package com.onclass.capacity_service.infrastructure.entrypoints.dto.response;

import com.onclass.capacity_service.domain.model.TechnologySummary;

import java.util.List;

public record CapacityWithTechDTO(
        Long id,
        String name,
        String description,
        List<TechnologySummary> technologies
) {
}
