package com.onclass.capacity_service.infrastructure.entrypoints.dto.request;

import java.util.List;

public record CapacityRequestDTO(String name, String description, List<Long> technologyIds) {
}
