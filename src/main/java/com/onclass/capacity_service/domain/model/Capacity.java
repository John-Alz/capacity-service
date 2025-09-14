package com.onclass.capacity_service.domain.model;

import java.util.List;

public record Capacity(Long id, String name, String description, List<Long> technologyIds) {

    public Capacity {
        technologyIds = (technologyIds == null) ? List.of() : List.copyOf(technologyIds);

    }

}
