package com.onclass.capacity_service.application.config;

import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.domain.usecase.CapacityUseCase;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.CapacityPersistenceAdapter;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.TechnologyLinksWebClientAdapter;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final CapacityRepository capacityRepository;
    private final CapacityEntityMapper capacityEntityMapper;
    private final WebClient technologyWebClient;


    @Bean
    public CapacityPersistencePort capacityPersistencePort() {
        return new CapacityPersistenceAdapter(capacityRepository, capacityEntityMapper);
    }

    @Bean
    public TechnologyLinksPort technologyLinksPort() {
        return new TechnologyLinksWebClientAdapter(technologyWebClient);
    }

    @Bean
    public CapacityServicePort capacityServicePort(CapacityPersistencePort capacityPersistencePort) {
        return new CapacityUseCase(capacityPersistencePort, technologyLinksPort());
    }

}
