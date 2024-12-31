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
package org.apache.causeway.core.metamodel.inspect.model;

import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Introspection;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.commons.internal.collections._Streams;
import org.apache.causeway.core.metamodel.specloader.SpecificationLoader;
import org.apache.causeway.schema.metamodel.v2.Annotation;
import org.apache.causeway.schema.metamodel.v2.DomainClassDto;
import org.apache.causeway.schema.metamodel.v2.MetamodelElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Named(TypeNode.LOGICAL_TYPE_NAME)
@DomainObject(
        nature=Nature.VIEW_MODEL,
        introspection = Introspection.ANNOTATION_REQUIRED)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class TypeNode extends MMNode {

    public static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE + ".TypeNode";

    @Inject protected transient SpecificationLoader specificationLoader;

    @Property
    @PropertyLayout(hidden = Where.EVERYWHERE)
    @Getter @Setter private DomainClassDto domainClassDto;

    @Override
    public String createTitle() {
        var title = lookupTitleAnnotation().map(Annotation::getValue)
                .orElseGet(()->domainClassDto.getId());
        return title;
    }

    @Override
    protected String iconSuffix() {
        return "";
    }

    @Override
    protected MetamodelElement metamodelElement() {
        return domainClassDto;
    }

    // -- TREE NODE STUFF

    @Getter @Setter @XmlTransient
    private MMNode parentNode;

    @Override
    public Stream<MMNode> streamChildNodes() {
        return _Streams.<MMNode>concat(

                Stream.of(
                        MMNodeFactory.facetGroup(domainClassDto.getFacets(), this)),

                domainClassDto.getActions().getAct()
                .stream()
                .map(action->MMNodeFactory.action(action, this)),

                domainClassDto.getProperties().getProp()
                .stream()
                .map(prop->MMNodeFactory.property(prop, this)),

                domainClassDto.getCollections().getColl()
                .stream()
                .map(coll->MMNodeFactory.collection(coll, this)));

    }

}
