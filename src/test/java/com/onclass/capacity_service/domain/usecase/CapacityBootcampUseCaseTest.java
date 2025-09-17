package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.api.CapacityBootcampServicePort;
import com.onclass.capacity_service.domain.model.BootcampCapacityDetachResult;
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

    // ========= addAll =========

    @Test
    void addAll_happyPath_delegatesAndCompletes() {
        Long bootcampId = 77L;
        List<Long> capacities = List.of(10L, 11L, 12L);
        when(persistencePort.addAll(bootcampId, capacities)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.addAll(bootcampId, capacities))
                .verifyComplete();

        verify(persistencePort).addAll(bootcampId, capacities);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void addAll_propagatesError_fromPersistence() {
        Long bootcampId = 99L;
        List<Long> capacities = List.of(1L, 2L, 3L);
        when(persistencePort.addAll(eq(bootcampId), eq(capacities)))
                .thenReturn(Mono.error(new RuntimeException("DB down")));

        StepVerifier.create(useCase.addAll(bootcampId, capacities))
                .expectErrorMatches(ex -> ex instanceof RuntimeException
                        && ex.getMessage().contains("DB down"))
                .verify();

        verify(persistencePort).addAll(bootcampId, capacities);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void addAll_passesExactIds_preservingOrder() {
        Long bootcampId = 5L;
        List<Long> capacities = List.of(3L, 8L, 13L, 21L);
        when(persistencePort.addAll(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.addAll(bootcampId, capacities)).verifyComplete();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass(List.class);
        verify(persistencePort).addAll(eq(bootcampId), idsCaptor.capture());
        assertThat(idsCaptor.getValue()).containsExactlyElementsOf(capacities);

        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void addAll_allowsEmptyList_andStillDelegates() {
        Long bootcampId = 42L;
        List<Long> capacities = List.of();
        when(persistencePort.addAll(bootcampId, capacities)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.addAll(bootcampId, capacities)).verifyComplete();

        verify(persistencePort).addAll(bootcampId, capacities);
        verifyNoMoreInteractions(persistencePort);
    }

    // ========= detachLinksByBootcamp =========

    @Test
    void detachLinksByBootcamp_happyPath_returnsResult_andDelegates() {
        Long bootcampId = 123L;
        var expected = new BootcampCapacityDetachResult(
                List.of(101L, 102L),   // detachedCapacityIds
                List.of(102L)          // orphanCapacityIds
        );

        when(persistencePort.detachLinksByBootcamp(bootcampId))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(useCase.detachLinksByBootcamp(bootcampId))
                .expectNext(expected)
                .verifyComplete();

        verify(persistencePort).detachLinksByBootcamp(bootcampId);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void detachLinksByBootcamp_propagatesError_fromPersistence() {
        Long bootcampId = 999L;
        when(persistencePort.detachLinksByBootcamp(bootcampId))
                .thenReturn(Mono.error(new IllegalStateException("constraint fail")));

        StepVerifier.create(useCase.detachLinksByBootcamp(bootcampId))
                .expectErrorMatches(ex -> ex instanceof IllegalStateException
                        && ex.getMessage().contains("constraint fail"))
                .verify();

        verify(persistencePort).detachLinksByBootcamp(bootcampId);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void detachLinksByBootcamp_passesExactBootcampId() {
        Long bootcampId = 7L;
        var result = new BootcampCapacityDetachResult(List.of(), List.of());
        when(persistencePort.detachLinksByBootcamp(anyLong()))
                .thenReturn(Mono.just(result));

        StepVerifier.create(useCase.detachLinksByBootcamp(bootcampId))
                .expectNext(result)
                .verifyComplete();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(persistencePort).detachLinksByBootcamp(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(bootcampId);

        verifyNoMoreInteractions(persistencePort);
    }
}
