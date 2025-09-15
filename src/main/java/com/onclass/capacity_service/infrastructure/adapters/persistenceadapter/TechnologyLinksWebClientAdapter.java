package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity_service.domain.model.TechnologySummary;
import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.dto.CapacityTechnologyRequestDTO;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.dto.TechnologySummaryDTO;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper.TechnologyLinksMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class TechnologyLinksWebClientAdapter implements TechnologyLinksPort {

    private final WebClient technologyWebClient;
    private final TechnologyLinksMapper technologyLinksMapper;

    @Override
    public Mono<Void> replaceAll(Long capacityId, List<Long> technologyIds) {
        System.out.println("TCHIDS MS CAP WEBCLIENTADAP: " + technologyIds);
        var body = new CapacityTechnologyRequestDTO(technologyIds);

        return technologyWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/capacity-technologies/{capacityId}")
                        .build(capacityId))
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<List<TechnologySummary>> listByCapacityId(Long capacityId) {
        return technologyWebClient.get()
                .uri("/capacity-technologies/{capacityId}", capacityId)
                .retrieve()
                .bodyToFlux(TechnologySummaryDTO.class)
                .collectList()
                .map(technologyLinksMapper::toModelList);
    }
}
