package com.onclass.capacity_service.infrastructure.entrypoints.exception;

import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime time, int status, String error, String message) {
}
