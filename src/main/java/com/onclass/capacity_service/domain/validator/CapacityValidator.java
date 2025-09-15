package com.onclass.capacity_service.domain.validator;

import com.onclass.capacity_service.domain.exception.ValidationException;
import com.onclass.capacity_service.domain.model.Capacity;
import com.onclass.capacity_service.domain.util.DomainConstants;

import java.util.HashSet;

public class CapacityValidator {

    public static void validateForCreate(Capacity capacity) {
        int n = capacity.technologyIds().size();

        if (n < 3) {
            throw new ValidationException(DomainConstants.MIN_LENGTH_TECH_MESSAGE);
        }
        if (n > 20) {
            throw new ValidationException(DomainConstants.MAX_LENGTH_TECH_MESSAGE);
        }
        if (n != new HashSet<>(capacity.technologyIds()).size()) {
            throw new ValidationException(DomainConstants.NO_REPEAT_TECH_MESSAGE);
        }
    }
}
