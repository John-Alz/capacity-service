package com.onclass.capacity_service.infrastructure.entrypoints;

import com.onclass.capacity_service.infrastructure.entrypoints.handler.CapacityBootcampHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class CapacityBootcampRouter {

    @Bean
    public RouterFunction<ServerResponse> capacityBootcampRoutes(CapacityBootcampHandler handler) {
        return RouterFunctions.route()
                .POST("/capacity-bootcamps/{bootcampId}", handler::saveCapacityBootcamp)
                .DELETE("/capacity-links/bootcamp/{bootcampId}", handler::detachByBootcamp)
                .build();
    }


}
