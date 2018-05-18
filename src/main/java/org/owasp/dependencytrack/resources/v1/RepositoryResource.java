/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.owasp.dependencytrack.resources.v1;

import alpine.persistence.PaginatedResult;
import alpine.resources.AlpineResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;
import org.owasp.dependencytrack.model.Repository;
import org.owasp.dependencytrack.model.RepositoryType;
import org.owasp.dependencytrack.persistence.QueryManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resources for processing repositories.
 *
 * @author Steve Springett
 * @since 3.1.0
 */
@Path("/v1/repository")
@Api(value = "repository", authorizations = @Authorization(value = "X-Api-Key"))
public class RepositoryResource extends AlpineResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Returns a list of all repositories",
            response = Repository.class,
            responseContainer = "List",
            responseHeaders = @ResponseHeader(name = TOTAL_COUNT_HEADER, response = Long.class, description = "The total number of repositories")
    )
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response getRepositories() {
        try (QueryManager qm = new QueryManager(getAlpineRequest())) {
            final PaginatedResult result = qm.getRepositories();
            return Response.ok(result.getObjects()).header(TOTAL_COUNT_HEADER, result.getTotal()).build();
        }
    }

    @GET
    @Path("/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Returns repositories that support the specific type",
            response = Repository.class,
            responseContainer = "List",
            responseHeaders = @ResponseHeader(name = TOTAL_COUNT_HEADER, response = Long.class, description = "The total number of repositories")

    )
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response getRepositoriesByType(
            @ApiParam(value = "The type of repositories to retrieve", required = true)
            @PathParam("type")RepositoryType type) {
        try (QueryManager qm = new QueryManager(getAlpineRequest())) {
            final PaginatedResult result = qm.getRepositories(type);
            return Response.ok(result.getObjects()).header(TOTAL_COUNT_HEADER, result.getTotal()).build();
        }
    }

}