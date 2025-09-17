package com.onclass.capacity_service.domain.model;

import java.util.List;

public record BootcampCapacityDetachResult(
        List<Long> detachedCapacityIds,
        List<Long> orphanCapacityIds
) {
}
