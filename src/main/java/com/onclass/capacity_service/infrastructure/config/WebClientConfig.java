package com.onclass.capacity_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient technologyWebClient(
            @Value("${technology.ms.base-url}")  String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}
