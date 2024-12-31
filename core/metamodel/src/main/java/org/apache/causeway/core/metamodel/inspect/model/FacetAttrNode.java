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
import org.apache.causeway.schema.metamodel.v2.FacetAttr;
import org.apache.causeway.schema.metamodel.v2.MetamodelElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Named(FacetAttrNode.LOGICAL_TYPE_NAME)
@DomainObject(
        nature=Nature.VIEW_MODEL,
        introspection = Introspection.ANNOTATION_REQUIRED
)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class FacetAttrNode extends MMNode {

    public static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE + ".node.FacetAttrNode";

    @Property
    @PropertyLayout(hidden = Where.EVERYWHERE)
    @Getter @Setter private FacetAttr facetAttr;

    @Override
    public String createTitle() {
        return String.format("%s: %s", facetAttr.getName(), facetAttr.getValue());
    }

    @Override
    protected String iconSuffix() {
        return "";
    }

    @Override
    protected MetamodelElement metamodelElement() {
        return facetAttr;
    }

    // -- TREE NODE STUFF

    @Getter @Setter @XmlTransient
    private FacetNode parentNode;

    @Override
    public Stream<MMNode> streamChildNodes() {
        return Stream.empty();
    }

}

