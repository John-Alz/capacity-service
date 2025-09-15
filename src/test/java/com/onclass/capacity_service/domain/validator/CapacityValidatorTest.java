package com.onclass.capacity_service.domain.validator;

import com.onclass.capacity_service.domain.exception.ValidationException;
import com.onclass.capacity_service.domain.model.Capacity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacityValidatorTest {

    @Test
    void validateForCreate_ok_when3UniqueTechs() {
        var capacity = new Capacity(
                null,
                "Backend",
                "Server side",
                List.of(1L, 2L, 3L) // 3 únicas
        );

        assertDoesNotThrow(() -> CapacityValidator.validateForCreate(capacity));
    }

    @Test
    void validateForCreate_throws_whenLessThan3() {
        var capacity = new Capacity(
                null,
                "Backend",
                "Server side",
                List.of(1L, 2L) // < 3
        );

        assertThrows(ValidationException.class,
                () -> CapacityValidator.validateForCreate(capacity));
    }

    @Test
    void validateForCreate_throws_whenMoreThan20() {
        // crea 21 IDs distintos
        List<Long> ids = new ArrayList<>();
        for (long i = 1; i <= 21; i++) ids.add(i);

        var capacity = new Capacity(
                null,
                "Data",
                "desc",
                ids
        );

        assertThrows(ValidationException.class,
                () -> CapacityValidator.validateForCreate(capacity));
    }

    @Test
    void validateForCreate_throws_whenDuplicates() {
        var capacity = new Capacity(
                null,
                "Mobile",
                "desc",
                List.of(10L, 11L, 10L) // duplicado 10
        );

        assertThrows(ValidationException.class,
                () -> CapacityValidator.validateForCreate(capacity));
    }
}
