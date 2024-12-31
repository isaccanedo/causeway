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
package org.apache.causeway.viewer.restfulobjects.jaxrsresteasy.conneg;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.Marshaller;

import org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlRootElementProvider;

import org.springframework.stereotype.Component;

import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.inject.ServiceInjector;

/**
 * @since 1.x {@index}
 */
@Component
@Provider
@Produces({"application/xml", "application/*+xml", "text/*+xml"})
public class RestfulObjectsJaxbWriterForXml extends JAXBXmlRootElementProvider {

    @Inject private ServiceInjector serviceInjector;

    @Override
    protected boolean isReadWritable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return super.isReadWritable(type, genericType, annotations, mediaType) &&
                hasXRoDomainTypeParameter(mediaType);
    }

    @Override
    protected Marshaller getMarshaller(
            final Class<?> type,
            final Annotation[] annotations,
            final MediaType mediaType) {

        var adapter = serviceInjector.injectServicesInto(new PersistentEntityAdapter());

        var marshaller = super.getMarshaller(type, annotations, mediaType);
        marshaller.setAdapter(PersistentEntityAdapter.class, adapter);
        return marshaller;
    }

    // HELPER

    private static boolean hasXRoDomainTypeParameter(final MediaType mediaType) {
        final boolean retval = mediaType.getParameters().containsKey("x-ro-domain-type");
        return retval;
    }

}
