package com.onclass.capacity_service.domain.usecase;

import com.onclass.capacity_service.domain.exception.ValidationException;
import com.onclass.capacity_service.domain.model.*;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.domain.util.DomainConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    CapacityPersistencePort capacityPersistencePort;

    @Mock
    TechnologyLinksPort technologyLinksPort;

    CapacityUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CapacityUseCase(capacityPersistencePort, technologyLinksPort);
    }

    // ---------- saveCapacity ----------

    @Test
    void saveCapacity_happyPath_callsReplaceAllAndReturnsSaved() {
        // given
        var req = new Capacity(null, "Backend", "Server side", List.of(1L, 2L, 3L));
        var saved = new Capacity(10L, "Backend", "Server side", List.of(1L, 2L, 3L));

        when(capacityPersistencePort.saveCapacity(req)).thenReturn(Mono.just(saved));
        when(technologyLinksPort.replaceAll(10L, List.of(1L, 2L, 3L))).thenReturn(Mono.empty());

        // when
        var mono = useCase.saveCapacity(req);

        // then
        StepVerifier.create(mono)
                .expectNext(saved)
                .verifyComplete();

        // verify replaceAll called with saved.id and original techIds
        verify(technologyLinksPort).replaceAll(10L, List.of(1L, 2L, 3L));
        verify(capacityPersistencePort).saveCapacity(req);
        verifyNoMoreInteractions(technologyLinksPort, capacityPersistencePort);
    }

    @Test
    void saveCapacity_validationFails_whenLessThan3Techs() {
        var req = new Capacity(null, "Backend", "Server side", List.of(1L, 2L));

        StepVerifier.create(Mono.defer(() -> useCase.saveCapacity(req)))
                .expectError(ValidationException.class)
                .verify();

        verifyNoInteractions(capacityPersistencePort, technologyLinksPort);
    }


    // ---------- listItems: validaciones ----------

    @Test
    void listItems_fails_whenPageNegative() {
        var mono = useCase.listItems(-1, 10, true, false, true);

        StepVerifier.create(mono)
                .expectErrorSatisfies(err -> {
                    assertThat(err).isInstanceOf(ValidationException.class);
                    assertThat(err.getMessage()).isEqualTo(DomainConstants.MIN_LENGTH_PAGE_MESSAGE);
                })
                .verify();
        verifyNoInteractions(capacityPersistencePort, technologyLinksPort);
    }

    @Test
    void listItems_fails_whenSizeZeroOrLess() {
        var mono = useCase.listItems(0, 0, true, false, true);

        StepVerifier.create(mono)
                .expectErrorSatisfies(err -> {
                    assertThat(err).isInstanceOf(ValidationException.class);
                    assertThat(err.getMessage()).isEqualTo(DomainConstants.MIN_LENGTH_SIZE_MESSAGE);
                })
                .verify();
        verifyNoInteractions(capacityPersistencePort, technologyLinksPort);
    }

    // ---------- listItems: composición y orden ----------

    @Test
    void listItems_buildsPageWithTechnologies_andSortsByNameAsc() {
        // given
        var capA = new Capacity(1L, "A-cap", "desc", List.of());
        var capB = new Capacity(2L, "B-cap", "desc", List.of());
        when(capacityPersistencePort.findPage(0, 10, true)).thenReturn(Flux.just(capB, capA)); // llega desordenado
        when(capacityPersistencePort.count()).thenReturn(Mono.just(2L));

        when(technologyLinksPort.listByCapacityId(1L))
                .thenReturn(Mono.just(List.of(new TechnologySummary(10L, "Java"))));
        when(technologyLinksPort.listByCapacityId(2L))
                .thenReturn(Mono.just(List.of(new TechnologySummary(20L, "Python"),
                        new TechnologySummary(21L, "Go"))));

        // when
        var mono = useCase.listItems(0, 10, true, false, true); // sort by name asc

        // then
        StepVerifier.create(mono)
                .assertNext(page -> {
                    assertThat(page.page()).isEqualTo(0);
                    assertThat(page.size()).isEqualTo(10);
                    assertThat(page.total()).isEqualTo(2);

                    var items = page.items();
                    assertThat(items).hasSize(2);
                    // orden por nombre asc => A-cap, B-cap
                    assertThat(items.get(0).name()).isEqualTo("A-cap");
                    assertThat(items.get(0).technologies()).extracting("name").containsExactly("Java");

                    assertThat(items.get(1).name()).isEqualTo("B-cap");
                    assertThat(items.get(1).technologies()).extracting("name").containsExactly("Python", "Go");
                })
                .verifyComplete();

        verify(capacityPersistencePort).findPage(0, 10, true);
        verify(capacityPersistencePort).count();
        verify(technologyLinksPort).listByCapacityId(1L);
        verify(technologyLinksPort).listByCapacityId(2L);
        verifyNoMoreInteractions(capacityPersistencePort, technologyLinksPort);
    }

    @Test
    void listItems_sortsByTechCount_desc() {
        // given
        var capX = new Capacity(100L, "X", "desc", List.of());
        var capY = new Capacity(200L, "Y", "desc", List.of());
        when(capacityPersistencePort.findPage(0, 10, true)).thenReturn(Flux.just(capX, capY));
        when(capacityPersistencePort.count()).thenReturn(Mono.just(2L));

        when(technologyLinksPort.listByCapacityId(100L))
                .thenReturn(Mono.just(List.of(new TechnologySummary(1L, "A")))); // 1 tech
        when(technologyLinksPort.listByCapacityId(200L))
                .thenReturn(Mono.just(List.of(new TechnologySummary(2L, "B"),
                        new TechnologySummary(3L, "C")))); // 2 techs

        // when: sortByTechCnt=true, ascTechCount=false (desc)
        var mono = useCase.listItems(0, 10, true, true, false);

        // then
        StepVerifier.create(mono)
                .assertNext(page -> {
                    var items = page.items();
                    assertThat(items).hasSize(2);
                    // desc por cantidad => Y (2), X (1)
                    assertThat(items.get(0).id()).isEqualTo(200L);
                    assertThat(items.get(1).id()).isEqualTo(100L);
                })
                .verifyComplete();
    }

    @Test
    void listItems_sortsByTechCount_asc_andEmptyListHandled() {
        // given
        var capEmpty = new Capacity(300L, "Empty", "desc", List.of());
        when(capacityPersistencePort.findPage(0, 5, true)).thenReturn(Flux.just(capEmpty));
        when(capacityPersistencePort.count()).thenReturn(Mono.just(1L));

        when(technologyLinksPort.listByCapacityId(300L))
                .thenReturn(Mono.just(List.of())); // sin tecnologías

        // when: sortByTechCnt=true ascTechCount=true
        var mono = useCase.listItems(0, 5, true, true, true);

        // then
        StepVerifier.create(mono)
                .assertNext(page -> {
                    assertThat(page.items()).hasSize(1);
                    assertThat(page.items().get(0).id()).isEqualTo(300L);
                    assertThat(page.items().get(0).technologies()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void saveCapacity_passesTheExactIdsToReplaceAll() {
        // given: sin duplicados para no disparar la validación
        var ids = List.of(5L, 6L, 7L, 8L);
        var req = new Capacity(null, "Data", "desc", ids);
        var saved = new Capacity(99L, "Data", "desc", ids);

        when(capacityPersistencePort.saveCapacity(req)).thenReturn(Mono.just(saved));
        when(technologyLinksPort.replaceAll(eq(99L), eq(ids))).thenReturn(Mono.empty());

        // when / then
        StepVerifier.create(useCase.saveCapacity(req))
                .expectNext(saved)
                .verifyComplete();

        // verifica que se pasaron EXACTAMENTE esos IDs y ese capacityId
        verify(technologyLinksPort).replaceAll(eq(99L), eq(ids));
        verifyNoMoreInteractions(technologyLinksPort);
    }

    // ---------- listCapacitiesSummary (coverage) ----------

    @Test
    void listCapacitiesSummary_returnsCapacitiesWithTheirTechnologies() {
        // given
        Long bootcampId = 777L;
        var cap1 = new Capacity(1L, "BackEnd", "desc", List.of());
        var cap2 = new Capacity(2L, "FrontEnd", "desc", List.of());

        when(capacityPersistencePort.findByBootcampId(bootcampId))
                .thenReturn(Flux.just(cap1, cap2));

        when(technologyLinksPort.listByCapacityId(1L))
                .thenReturn(Mono.just(List.of(
                        new TechnologySummary(10L, "Java"),
                        new TechnologySummary(11L, ".NET")
                )));
        when(technologyLinksPort.listByCapacityId(2L))
                .thenReturn(Mono.just(List.of(
                        new TechnologySummary(20L, "Angular"),
                        new TechnologySummary(21L, "React")
                )));

        // when
        var mono = useCase.listCapacitiesSummary(bootcampId);

        // then
        StepVerifier.create(mono)
                .assertNext(list -> {
                    assertThat(list).hasSize(2);

                    var first = list.get(0);
                    assertThat(first.id()).isEqualTo(1L);
                    assertThat(first.name()).isEqualTo("BackEnd");
                    assertThat(first.technologies()).extracting("name")
                            .containsExactly("Java", ".NET");

                    var second = list.get(1);
                    assertThat(second.id()).isEqualTo(2L);
                    assertThat(second.name()).isEqualTo("FrontEnd");
                    assertThat(second.technologies()).extracting("name")
                            .containsExactly("Angular", "React");
                })
                .verifyComplete();

        verify(capacityPersistencePort).findByBootcampId(bootcampId);
        verify(technologyLinksPort).listByCapacityId(1L);
        verify(technologyLinksPort).listByCapacityId(2L);
        verifyNoMoreInteractions(capacityPersistencePort, technologyLinksPort);
    }

    @Test
    void listCapacitiesSummary_handlesEmptyTechnologies() {
        // given
        Long bootcampId = 888L;
        var cap = new Capacity(41L, "Data Eng", "desc", List.of());

        when(capacityPersistencePort.findByBootcampId(bootcampId))
                .thenReturn(Flux.just(cap));

        // Simulamos que el MS de tech devuelve Mono.empty()
        when(technologyLinksPort.listByCapacityId(41L)).thenReturn(Mono.empty());

        // when
        var mono = useCase.listCapacitiesSummary(bootcampId);

        // then
        StepVerifier.create(mono)
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    var only = list.get(0);
                    assertThat(only.id()).isEqualTo(41L);
                    assertThat(only.name()).isEqualTo("Data Eng");
                    // defaultIfEmpty(List.of()) => lista vacía, no null
                    assertThat(only.technologies()).isEmpty();
                })
                .verifyComplete();

        verify(capacityPersistencePort).findByBootcampId(bootcampId);
        verify(technologyLinksPort).listByCapacityId(41L);
        verifyNoMoreInteractions(capacityPersistencePort, technologyLinksPort);
    }
}
