package com.onclass.capacity_service.infrastructure.entrypoints;

import com.onclass.capacity_service.infrastructure.entrypoints.handler.CapacityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class CapacityRest {

    @Bean
    public RouterFunction<ServerResponse> capacityRoutes(CapacityHandler capacityHandler) {
        return route(POST("/capacity"), capacityHandler::createCapacity);
    }

    @Bean
    public RouterFunction<ServerResponse> capacitiesRoutes(CapacityHandler capacityHandler) {
        return route(GET("/capacities"), capacityHandler::listCapacity);
    }

}
