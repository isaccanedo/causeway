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
package org.apache.causeway.applib.mixins.dto;

import java.util.Map;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.RestrictTo;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.jaxb.CausewaySchemas;
import org.apache.causeway.applib.services.jaxb.JaxbService;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.value.Blob;
import org.apache.causeway.applib.value.Clob;
import org.apache.causeway.applib.value.NamedWithMimeType.CommonMimeType;
import org.apache.causeway.commons.io.ZipUtils;

import lombok.RequiredArgsConstructor;

/**
 * Mixin that provides the ability to download the XSD schema for a view model
 * can be downloaded as XML.
 *
 * <p>
 *  Requires that the view model is a JAXB view model, and implements the
 *  {@link Dto} marker interface.
 * </p>
 *
 * <p>
 * If the domain object's JAXB annotations reference only a single XSD schema
 * then this will return that XML text as a {@link Clob} of that XSD.
 * If there are multiple XSD schemas referenced then the action will return a
 * zip of those schemas, wrapped up in a {@link Blob}.
 * </p>
 *
 * @since 1.x {@index}
 */
@Action(
        commandPublishing = Publishing.DISABLED,
        domainEvent = Dto_downloadXsd.ActionDomainEvent.class,
        executionPublishing = Publishing.DISABLED,
        restrictTo = RestrictTo.PROTOTYPING,
        semantics = SemanticsOf.SAFE
        )
@ActionLayout(
        cssClassFa = "fa-download",
        sequence = "500.2")
//mixin's don't need a logicalTypeName, in fact MM validation should guard against wrong usage here
@RequiredArgsConstructor
public class Dto_downloadXsd {

    private final Dto holder;

    public static class ActionDomainEvent
    extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Dto_downloadXsd> {}

    /**
     * The {@link CausewaySchemas} parameter can be used to optionally ignore the
     * common Apache Causeway schemas; useful if there is only one other XSD schema
     * referenced by the DTO.
     */
    @MemberSupport public Object act(

            @ParameterLayout(
                    named = DtoMixinConstants.FILENAME_PROPERTY_NAME,
                    describedAs = DtoMixinConstants.FILENAME_PROPERTY_DESCRIPTION)
            final String fileName,

            final CausewaySchemas causewaySchemas) {

        var schemaMap = jaxbService.toXsd(holder, causewaySchemas);

        if(schemaMap.isEmpty()) {
            var msg = String.format(
                    "No schemas were generated for %s; programming error?",
                    holder.getClass().getName());
            messageService.warnUser(msg);
            return null;
        }

        if(schemaMap.size() == 1) {
            var xmlString = schemaMap.values().iterator().next();
            return Clob.of(fileName, CommonMimeType.XSD, xmlString);
        }

        var zipBuilder = ZipUtils.zipEntryBuilder();

        for (Map.Entry<String, String> entry : schemaMap.entrySet()) {
            var namespaceUri = entry.getKey();
            var schemaText = entry.getValue();
            zipBuilder.addAsUtf8(zipEntryNameFor(namespaceUri), schemaText);
        }

        return Blob.of(fileName, CommonMimeType.ZIP, zipBuilder.toBytes());

    }

    /**
     * Defaults to the fully qualified class name of the domain object.
     */
    @MemberSupport public String default0Act() {
        return holder.getClass().getName();
    }

    /**
     * Defaults to {@link CausewaySchemas#IGNORE}
     */
    @MemberSupport public CausewaySchemas default1Act() {
        return CausewaySchemas.IGNORE;
    }

    private static String zipEntryNameFor(final String namespaceUri) {
        return namespaceUri + ".xsd";
    }

    @Inject MessageService messageService;
    @Inject JaxbService jaxbService;
}

