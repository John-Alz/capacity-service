package com.onclass.capacity_service.infrastructure.entrypoints;

import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityBootcampRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.response.SuccessReponseDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.handler.CapacityBootcampHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "Capacity-Bootcamps", description = "Relaciones entre capacidades y bootcamps")
public class CapacityBootcampRouter {

    @RouterOperations({
            @RouterOperation(
                    path = "/capacity-bootcamps/{bootcampId}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = CapacityBootcampHandler.class,
                    beanMethod = "saveCapacityBootcamp",
                    operation = @Operation(
                            operationId = "saveCapacityBootcamp",
                            summary = "Crear/actualizar relación de capacidades para un bootcamp",
                            description = "Asigna (agrega) la lista de capacidades al bootcamp indicado por `bootcampId`.",
                            parameters = {
                                    @Parameter(
                                            name = "bootcampId",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "ID del bootcamp",
                                            example = "100"
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CapacityBootcampRequestDTO.class),
                                            examples = @ExampleObject(
                                                    name = "CapacityBootcampRequest",
                                                    value = """
                                    {
                                      "capacitiesId": [39, 40, 41]
                                    }
                                    """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Relación creada",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = SuccessReponseDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "SuccessResponse",
                                                            value = """
                                        {
                                          "message": "Relacion Capacity Bootcamp Creado.",
                                          "timestamp": "2025-09-19T10:15:30"
                                        }
                                        """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación",
                                            content = @Content
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno",
                                            content = @Content
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/capacity-links/bootcamp/{bootcampId}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.DELETE,
                    beanClass = CapacityBootcampHandler.class,
                    beanMethod = "detachByBootcamp",
                    operation = @Operation(
                            operationId = "detachCapacityLinksByBootcamp",
                            summary = "Desvincular capacidades de un bootcamp",
                            description = "Elimina los vínculos de capacidades para el `bootcampId` indicado.",
                            parameters = {
                                    @Parameter(
                                            name = "bootcampId",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "ID del bootcamp",
                                            example = "100"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Resultado de la desvinculación",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = Object.class),
                                                    examples = @ExampleObject(
                                                            name = "DetachResult",
                                                            value = """
                                        {
                                          "detached": 3,
                                          "bootcampId": 100
                                        }
                                        """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación",
                                            content = @Content
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno",
                                            content = @Content
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> capacityBootcampRoutes(CapacityBootcampHandler handler) {
        return RouterFunctions.route()
                .POST("/capacity-bootcamps/{bootcampId}", handler::saveCapacityBootcamp)
                .DELETE("/capacity-links/bootcamp/{bootcampId}", handler::detachByBootcamp)
                .build();
    }
}
