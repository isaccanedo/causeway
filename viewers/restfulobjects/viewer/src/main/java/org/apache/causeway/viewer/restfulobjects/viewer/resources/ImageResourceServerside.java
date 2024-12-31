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
package org.apache.causeway.viewer.restfulobjects.viewer.resources;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import org.apache.causeway.commons.internal.base._Bytes;
import org.apache.causeway.commons.internal.resources._Resources;

import lombok.extern.log4j.Log4j2;

@Component
@Path("/image")
@Log4j2
public class ImageResourceServerside extends ResourceAbstract {

    public ImageResourceServerside() {
        super();
        log.debug("<init>");
    }

    @GET
    @Path("/")
    @Produces("image/png")
    public Response image() throws IOException {

        final InputStream resource = _Resources.load(getClass(), "SimpleObject.png");
        final byte[] bytes = _Bytes.of(resource);

        //        Response.ResponseBuilder response = Response.ok(file);
        //        response.header("Content-Disposition",
        //                "attachment; filename=SimpleObject.png");
        //        return response.build();

        return _EndpointLogging.response(log, "GET /image",
                Response.ok(bytes).build());
    }
}
