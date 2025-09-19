package com.onclass.capacity_service.infrastructure.entrypoints;

import com.onclass.capacity_service.infrastructure.entrypoints.handler.CapacityHandler;
import com.onclass.capacity_service.infrastructure.entrypoints.dto.request.CapacityRequestDTO;
import com.onclass.capacity_service.infrastructure.entrypoints.exception.ExceptionResponse;
import com.onclass.capacity_service.domain.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@Tag(name = "Capacities", description = "Operaciones sobre capacidades")
public class CapacityRest {

    @RouterOperations({
            @RouterOperation(
                    path = "/capacity",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = CapacityHandler.class,
                    beanMethod = "createCapacity",
                    operation = @Operation(
                            operationId = "createCapacity",
                            summary = "Crear capacidad",
                            description = "Crea una nueva capacidad.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CapacityRequestDTO.class),
                                            examples = @ExampleObject(
                                                    name = "CapacityRequest",
                                                    value = """
                                    {
                                      "name": "Backend Developer",
                                      "description": "Capacidad orientada a servicios y APIs"
                                    }
                                    """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Capacidad creada",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = Object.class),
                                                    examples = @ExampleObject(
                                                            name = "CapacityResponse",
                                                            value = """
                                        {
                                          "id": 39,
                                          "name": "Backend Developer",
                                          "description": "Capacidad orientada a servicios y APIs",
                                          "technologyCount": 0
                                        }
                                        """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/capacities",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = CapacityHandler.class,
                    beanMethod = "listCapacity",
                    operation = @Operation(
                            operationId = "listCapacities",
                            summary = "Listar capacidades (paginado y ordenado)",
                            description = """
                    Retorna un PageResult con las capacidades. 
                    Parámetros:
                    - page (default 0)
                    - size (default 10)
                    - sortBy: 'name' o 'technologyCount' (default 'name')
                    - order: 'asc' o 'desc' (default 'asc')
                    """,
                            parameters = {
                                    @Parameter(name = "page", in = ParameterIn.QUERY, required = false, example = "0"),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, required = false, example = "10"),
                                    @Parameter(name = "sortBy", in = ParameterIn.QUERY, required = false, example = "name"),
                                    @Parameter(name = "order", in = ParameterIn.QUERY, required = false, example = "asc")
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Página de capacidades",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = PageResult.class),
                                                    examples = @ExampleObject(
                                                            name = "PageResult",
                                                            value = """
                                        {
                                          "page": 0,
                                          "size": 10,
                                          "total": 2,
                                          "items": [
                                            {
                                              "id": 39,
                                              "name": "Backend Developer",
                                              "description": "Capacidad orientada a servicios y APIs",
                                              "technologyCount": 3
                                            },
                                            {
                                              "id": 40,
                                              "name": "Frontend Developer",
                                              "description": "Capacidad orientada a interfaces",
                                              "technologyCount": 2
                                            }
                                          ]
                                        }
                                        """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación",
                                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/capacities-bootcamp/{bootcampId}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = CapacityHandler.class,
                    beanMethod = "listCapacitiesSummary",
                    operation = @Operation(
                            operationId = "listCapacitiesSummaryByBootcamp",
                            summary = "Resumen de capacidades por bootcamp",
                            description = "Devuelve un listado resumen de capacidades asociadas al bootcamp indicado.",
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
                                            description = "Lista de capacidades (resumen)",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = Object.class)),
                                                    examples = @ExampleObject(
                                                            name = "CapacitySummaryList",
                                                            value = """
                                        [
                                          { "id": 39, "name": "Backend Developer" },
                                          { "id": 40, "name": "Frontend Developer" }
                                        ]
                                        """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/capacities",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.DELETE,
                    beanClass = CapacityHandler.class,
                    beanMethod = "deleteByIds",
                    operation = @Operation(
                            operationId = "deleteCapacitiesByIds",
                            summary = "Eliminar capacidades por IDs",
                            description = "Elimina capacidades por una lista de IDs separados por coma en el query param `ids`.",
                            parameters = {
                                    @Parameter(
                                            name = "ids",
                                            in = ParameterIn.QUERY,
                                            required = false,
                                            description = "IDs separados por coma. Ej: `39,40,41`",
                                            example = "39,40,41"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Resultado de la eliminación",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = Object.class),
                                                    examples = @ExampleObject(
                                                            name = "DeleteResult",
                                                            value = """
                                        {
                                          "deleted": 2,
                                          "requested": 3,
                                          "notFound": [41]
                                        }
                                        """
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> capacityRoutes(CapacityHandler capacityHandler) {
        return RouterFunctions.route()
                .POST("/capacity", capacityHandler::createCapacity)
                .DELETE("/capacities", capacityHandler::deleteByIds)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> capacitiesRoutes(CapacityHandler capacityHandler) {
        return route(GET("/capacities"), capacityHandler::listCapacity);
    }

    @Bean
    public RouterFunction<ServerResponse> capacitiesSummaryRoutes(CapacityHandler capacityHandler) {
        return route(GET("/capacities-bootcamp/{bootcampId}"), capacityHandler::listCapacitiesSummary);
    }
}
