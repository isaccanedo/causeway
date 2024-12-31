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

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.RestrictTo;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.jaxb.JaxbService;
import org.apache.causeway.applib.value.Clob;
import org.apache.causeway.applib.value.NamedWithMimeType.CommonMimeType;

import lombok.RequiredArgsConstructor;

/**
 * Mixin that provides the ability to download a view model as XML.
 *
 * <p>
 *  Requires that the view model is a JAXB view model, and implements the
 *  {@link Dto} marker interface.
 * </p>
 *
 * @see Dto_downloadXsd
 *
 * @since 1.x {@index}
 */
@Action(
        commandPublishing = Publishing.DISABLED,
        domainEvent = Dto_downloadXml.ActionDomainEvent.class,
        executionPublishing = Publishing.DISABLED,
        restrictTo = RestrictTo.PROTOTYPING,
        semantics = SemanticsOf.SAFE
        )
@ActionLayout(
        cssClassFa = "fa-download",
        sequence = "500.1")
//mixin's don't need a logicalTypeName, in fact MM validation should guard against wrong usage here
@RequiredArgsConstructor
public class Dto_downloadXml {

    private final Dto holder;

    public static class ActionDomainEvent
    extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Dto_downloadXml> {}

    @MemberSupport public Object act(

            // PARAM 0
            @ParameterLayout(
                    named = DtoMixinConstants.FILENAME_PROPERTY_NAME,
                    describedAs = DtoMixinConstants.FILENAME_PROPERTY_DESCRIPTION)
            final String fileName) {

        var xmlString = jaxbService.toXml(holder);
        return Clob.of(fileName, CommonMimeType.XML, xmlString);
    }

    @MemberSupport public String default0Act() {
        return holder.getClass().getName();
    }

    @Inject JaxbService jaxbService;

}
