package com.onclass.capacity_service.infrastructure.entrypoints.dto.response;

import java.time.LocalDateTime;

public record SuccessReponseDTO(String message, LocalDateTime time) {
}
