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
package org.apache.causeway.core.metamodel.facets.actions.layout;

import java.util.Optional;

import org.apache.causeway.applib.annotation.Redirect;
import org.apache.causeway.applib.layout.component.ActionLayoutData;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.actions.redirect.RedirectFacet;
import org.apache.causeway.core.metamodel.facets.actions.redirect.RedirectFacetAbstract;

public class RedirectFacetFromActionLayoutXml
extends RedirectFacetAbstract {

    public static Optional<RedirectFacet> create(
            final ActionLayoutData actionLayout,
            final FacetHolder holder,
            final Precedence precedence) {
        if(actionLayout == null) {
            return Optional.empty();
        }
        final Redirect redirect = actionLayout.getRedirect();
        return redirect != null
                ? Optional.of(new RedirectFacetFromActionLayoutXml(redirect, holder, precedence))
                : Optional.empty();
    }

    public RedirectFacetFromActionLayoutXml(
            final Redirect policy, final FacetHolder holder, final Precedence precedence) {
        super(policy, holder, precedence);
    }

    @Override
    public boolean isObjectTypeSpecific() {
        return true;
    }

}
