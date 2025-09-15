package com.onclass.capacity_service.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CapacityBootcampTest {

    @Test
    void capacityBootcamp_record_basicAccessors() {
        var rel = new CapacityBootcamp(1L, 2L, 3L);

        assertThat(rel.id()).isEqualTo(1L);
        assertThat(rel.capacityId()).isEqualTo(2L);
        assertThat(rel.bootcampId()).isEqualTo(3L);
    }

    @Test
    void capacityBootcamp_record_equalsAndHashCode() {
        var a1 = new CapacityBootcamp(1L, 2L, 3L);
        var a2 = new CapacityBootcamp(1L, 2L, 3L);
        var b  = new CapacityBootcamp(1L, 2L, 4L);

        assertThat(a1).isEqualTo(a2);
        assertThat(a1).hasSameHashCodeAs(a2);

        assertThat(a1).isNotEqualTo(b);
    }

    @Test
    void capacityBootcamp_record_toStringContainsData() {
        var rel = new CapacityBootcamp(10L, 20L, 30L);

        assertThat(rel.toString())
                .contains("CapacityBootcamp")
                .contains("10")
                .contains("20")
                .contains("30");
    }
}
