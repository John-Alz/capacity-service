package com.onclass.capacity_service.infrastructure.adapters.persistenceadapter;

import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.dto.CapacityTechnologyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class TechnologyLinksWebClientAdapter implements TechnologyLinksPort {

    private final WebClient technologyWebClient;

    @Override
    public Mono<Void> replaceAll(Long capacityId, List<Long> technologyIds) {
        System.out.println("TCHIDS MS CAP WEBCLIENTADAP: " + technologyIds);
        var body = new CapacityTechnologyRequestDTO(technologyIds);

        return technologyWebClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/capacity-technologies/{capacityId}")
                        .build(capacityId))
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
