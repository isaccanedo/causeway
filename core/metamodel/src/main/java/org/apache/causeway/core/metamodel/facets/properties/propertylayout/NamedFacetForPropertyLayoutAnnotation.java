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
package org.apache.causeway.core.metamodel.facets.properties.propertylayout;

import java.util.Optional;

import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.all.named.MemberNamedFacet;
import org.apache.causeway.core.metamodel.facets.all.named.MemberNamedFacetWithStaticTextAbstract;

public class NamedFacetForPropertyLayoutAnnotation
extends MemberNamedFacetWithStaticTextAbstract {

    public static Optional<MemberNamedFacet> create(
            final Optional<PropertyLayout> propertyLayoutIfAny,
            final FacetHolder holder) {

        return propertyLayoutIfAny
                .filter(propertyLayout->_Strings.isNotEmpty(propertyLayout.named()))
                .map(propertyLayout ->
                    new NamedFacetForPropertyLayoutAnnotation(
                        propertyLayout.named(),
                        holder));
    }

    private NamedFacetForPropertyLayoutAnnotation(
            final String named,
            final FacetHolder holder) {

        super(named, holder);
    }

}
