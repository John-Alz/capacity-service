package com.onclass.capacity_service.infrastructure.entrypoints.dto.response;

import java.util.List;

public record CapacitySummaryResponseDTO(Long id, String  name, List<TecnologiesSummaryResponseDTO> technologies) {
}
