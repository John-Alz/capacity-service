package com.onclass.capacity_service.domain.model;

import java.util.List;

public record DeleteBatchResult(
        List<Long> deletedIds,
        List<Long> notFoundIds
) {
}
