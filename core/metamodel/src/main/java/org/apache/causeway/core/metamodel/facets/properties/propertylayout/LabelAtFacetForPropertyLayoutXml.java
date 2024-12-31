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

import org.apache.causeway.applib.annotation.LabelPosition;
import org.apache.causeway.applib.layout.component.PropertyLayoutData;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.objectvalue.labelat.LabelAtFacet;
import org.apache.causeway.core.metamodel.facets.objectvalue.labelat.LabelAtFacetAbstract;

public class LabelAtFacetForPropertyLayoutXml
extends LabelAtFacetAbstract {

    public static Optional<LabelAtFacet> create(
            final PropertyLayoutData propertyLayout,
            final FacetHolder holder,
            final Precedence precedence) {
        if (propertyLayout == null) {
            return Optional.empty();
        }
        final LabelPosition labelPosition = propertyLayout.getLabelPosition();
        return labelPosition != null
                ? Optional.of(new LabelAtFacetForPropertyLayoutXml(labelPosition, holder, precedence))
                : Optional.empty();
    }

    private LabelAtFacetForPropertyLayoutXml(
            final LabelPosition value, final FacetHolder holder, final Precedence precedence) {
        super(value, holder, precedence);
    }

    @Override
    public boolean isObjectTypeSpecific() {
        return true;
    }

}
