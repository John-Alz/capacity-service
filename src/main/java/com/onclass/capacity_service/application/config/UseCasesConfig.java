package com.onclass.capacity_service.application.config;

import com.onclass.capacity_service.domain.api.CapacityBootcampServicePort;
import com.onclass.capacity_service.domain.api.CapacityServicePort;
import com.onclass.capacity_service.domain.spi.CapacityBootcampPersistencePort;
import com.onclass.capacity_service.domain.spi.CapacityPersistencePort;
import com.onclass.capacity_service.domain.spi.TechnologyLinksPort;
import com.onclass.capacity_service.domain.usecase.CapacityBootcampUseCase;
import com.onclass.capacity_service.domain.usecase.CapacityUseCase;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.CapacityBootcampPersistenceAdapter;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.CapacityPersistenceAdapter;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.TechnologyLinksWebClientAdapter;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.mapper.TechnologyLinksMapper;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityBootcampRepository;
import com.onclass.capacity_service.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final CapacityRepository capacityRepository;
    private final CapacityEntityMapper capacityEntityMapper;
    private final WebClient technologyWebClient;
    private final DatabaseClient db;
    private final TechnologyLinksMapper technologyLinksMapper;
    private final CapacityBootcampRepository capacityBootcampRepository;


    @Bean
    public CapacityPersistencePort capacityPersistencePort() {
        return new CapacityPersistenceAdapter(capacityRepository, capacityEntityMapper, db);
    }

    @Bean
    public TechnologyLinksPort technologyLinksPort() {
        return new TechnologyLinksWebClientAdapter(technologyWebClient, technologyLinksMapper);
    }

    @Bean
    public CapacityServicePort capacityServicePort(CapacityPersistencePort capacityPersistencePort) {
        return new CapacityUseCase(capacityPersistencePort, technologyLinksPort());
    }

    @Bean
    public CapacityBootcampPersistencePort capacityBootcampPersistencePort() {
        return new CapacityBootcampPersistenceAdapter(capacityBootcampRepository, db);
    }

    @Bean
    public CapacityBootcampServicePort capacityBootcampServicePort() {
        return new CapacityBootcampUseCase(capacityBootcampPersistencePort());
    }

}
