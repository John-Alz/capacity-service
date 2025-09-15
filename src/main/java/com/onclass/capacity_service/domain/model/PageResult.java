package com.onclass.capacity_service.domain.model;

import java.util.List;

public record PageResult<T>(int page, int size, long total, List<T> items) { }

