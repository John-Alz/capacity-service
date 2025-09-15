package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.api.CapacityBootcampServicePort;
import com.onclass.capacity_service.domain.spi.CapacityBootcampPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CapacityBootcampUseCaseTest {

    private CapacityBootcampPersistencePort persistencePort;
    private CapacityBootcampServicePort useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(CapacityBootcampPersistencePort.class);
        useCase = new CapacityBootcampUseCase(persistencePort);
    }

    @Test
    void addAll_happyPath_delegatesAndCompletes() {
        // given
        Long bootcampId = 77L;
        List<Long> capacities = List.of(10L, 11L, 12L);
        when(persistencePort.addAll(bootcampId, capacities)).thenReturn(Mono.empty());

        // when & then
        StepVerifier.create(useCase.addAll(bootcampId, capacities))
                .verifyComplete();

        verify(persistencePort).addAll(bootcampId, capacities);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void addAll_propagatesError_fromPersistence() {
        // given
        Long bootcampId = 99L;
        List<Long> capacities = List.of(1L, 2L, 3L);
        when(persistencePort.addAll(eq(bootcampId), eq(capacities)))
                .thenReturn(Mono.error(new RuntimeException("DB down")));

        // when & then
        StepVerifier.create(useCase.addAll(bootcampId, capacities))
                .expectErrorMatches(ex -> ex instanceof RuntimeException
                        && ex.getMessage().contains("DB down"))
                .verify();

        verify(persistencePort).addAll(bootcampId, capacities);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void addAll_passesExactIds_preservingOrder() {
        // given
        Long bootcampId = 5L;
        List<Long> capacities = List.of(3L, 8L, 13L, 21L);
        when(persistencePort.addAll(anyLong(), anyList())).thenReturn(Mono.empty());

        // when
        StepVerifier.create(useCase.addAll(bootcampId, capacities))
                .verifyComplete();

        // then: capturamos la lista para verificar orden y valores exactos
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass(List.class);
        verify(persistencePort).addAll(eq(bootcampId), idsCaptor.capture());

        assertThat(idsCaptor.getValue()).containsExactlyElementsOf(capacities);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void addAll_allowsEmptyList_andStillDelegates() {
        // given
        Long bootcampId = 42L;
        List<Long> capacities = List.of(); // vacío
        when(persistencePort.addAll(bootcampId, capacities)).thenReturn(Mono.empty());

        // when & then
        StepVerifier.create(useCase.addAll(bootcampId, capacities))
                .verifyComplete();

        verify(persistencePort).addAll(bootcampId, capacities);
        verifyNoMoreInteractions(persistencePort);
    }
}
