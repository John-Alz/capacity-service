package com.onclass.capacity_service.domain.model;

import java.util.List;

public record CapacityWithTechs(
        Long id,
        String name,
        String description,
        List<TechnologySummary> technologies
) {}
