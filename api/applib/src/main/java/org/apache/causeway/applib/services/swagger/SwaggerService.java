/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.applib.services.swagger;

/**
 * Generates Swagger schema definition files to describe the public and/or
 * private RESTful APIs exposed by the RestfulObjects viewer.
 *
 * <p>
 *     These spec files can then be used with the
 *     link:http://swagger.io/swagger-ui/[Swagger UI] page to explore the
 *     REST API, or used to generate client-side stubs using the
 *     link:http://swagger.io/swagger-codegen/[Swagger codegen] tool,
 *     eg for use in a custom REST client app.
 * </p>
 *
 * @since 1.x {@index}
 */
public interface SwaggerService {

    /**
     * Generate a Swagger spec with the specified visibility and format.
     *
     * @param visibility
     * @param format
     */
    String generateSwaggerSpec(
            final Visibility visibility,
            final Format format);

}
