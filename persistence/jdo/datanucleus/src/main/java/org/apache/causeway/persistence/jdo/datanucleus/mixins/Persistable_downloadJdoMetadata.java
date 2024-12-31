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
 *
 */
package org.apache.causeway.persistence.jdo.datanucleus.mixins;

import java.io.IOException;

import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.metadata.TypeMetadata;
import javax.xml.bind.JAXBException;

import org.datanucleus.enhancement.Persistable;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.RestrictTo;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.layout.LayoutConstants;
import org.apache.causeway.applib.value.Clob;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.persistence.jdo.applib.services.JdoSupportService;

import lombok.RequiredArgsConstructor;

/**
 * Provides the ability to download the JDO
 * <a href="http://www.datanucleus.org/products/datanucleus/jdo/metadata_xml.html">class metadata</a>
 * as XML.
 *
 * @since 2.0 {@index}
 */
@Action(
        commandPublishing = Publishing.DISABLED,
        domainEvent = Persistable_downloadJdoMetadata.ActionDomainEvent.class,
        executionPublishing = Publishing.DISABLED,
        restrictTo = RestrictTo.PROTOTYPING,
        semantics = SemanticsOf.SAFE
)
@ActionLayout(
        cssClassFa = "fa-download",
        describedAs = "Downloads the DataNucleus metamodel for this class (as a Xxx.jdo file).  One use case is to tactically override annotation-based configuration of existing entities, eg to target specific databases",
        fieldSetId = LayoutConstants.FieldSetId.METADATA,
        position = ActionLayout.Position.PANEL_DROPDOWN,
        sequence = "710.1"
)
@RequiredArgsConstructor
public class Persistable_downloadJdoMetadata {

    @Inject JdoSupportService jdoSupport;

    private final Persistable persistable;

    public static class ActionDomainEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Persistable_downloadJdoMetadata> {}

    @MemberSupport public Clob act(
            final String fileName) throws JAXBException, IOException {

        final Class<? extends Persistable> objClass = persistable.getClass();
        final String objClassName = objClass.getName();

        final TypeMetadata metadata = getPersistenceManagerFactory().getMetadata(objClassName);
        final String xml = metadata.toString();

        return new Clob(_Strings.asFileNameWithExtension(fileName, "jdo"), "text/xml", xml);
    }

    @MemberSupport public String default0Act() {
        return _Strings.asFileNameWithExtension(persistable.getClass().getName(), "jdo");
    }

    private PersistenceManagerFactory getPersistenceManagerFactory() {
        return jdoSupport.getPersistenceManager().getPersistenceManagerFactory();
    }

}
