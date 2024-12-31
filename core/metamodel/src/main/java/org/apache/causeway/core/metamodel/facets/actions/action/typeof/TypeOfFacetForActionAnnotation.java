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
package org.apache.causeway.core.metamodel.facets.actions.action.typeof;

import java.util.Optional;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.commons.internal.reflection._GenericResolver.ResolvedType;
import org.apache.causeway.commons.semantics.CollectionSemantics;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.apache.causeway.core.metamodel.facets.actcoll.typeof.TypeOfFacetAbstract;

public class TypeOfFacetForActionAnnotation
extends TypeOfFacetAbstract {

    public static Optional<TypeOfFacet> create(
            final Optional<Action> actionIfAny,
            final CollectionSemantics collectionSemantics,
            final FacetHolder facetHolder) {

        return actionIfAny
                .map(Action::typeOf)
                .filter(typeOf -> typeOf!=null
                                        && typeOf != void.class) // ignore when unspecified
                .map(typeOf ->
                    new TypeOfFacetForActionAnnotation(
                            ResolvedType
                                .plural(typeOf, collectionSemantics.getContainerType(), collectionSemantics),
                            facetHolder));
    }

    private TypeOfFacetForActionAnnotation(
            final ResolvedType type,
            final FacetHolder holder) {
        // overrules any generic type argument resolution that is based on reflection
        super(type, holder, Precedence.HIGH);
    }

}
