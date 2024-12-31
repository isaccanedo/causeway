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
package org.apache.causeway.core.metamodel.spec.feature;

import org.apache.causeway.commons.internal.exceptions._Exceptions;
import org.apache.causeway.commons.internal.reflection._GenericResolver.ResolvedType;
import org.apache.causeway.commons.semantics.CollectionSemantics;

/**
 * Base interface for {@link OneToManyAssociation} only.
 *
 * <p>
 * Introduced for symmetry with {@link OneToOneFeature}; if we ever support
 * collections as parameters then would also be the base interface for a
 * <tt>OneToManyActionParameter</tt>.
 *
 * <p>
 * Is also the route up to the {@link ObjectFeature} superinterface.
 *
 */
public interface OneToManyFeature extends ObjectFeature {

    ResolvedType getTypeOfAnyCardinality();

    default CollectionSemantics getCollectionSemantics() {
        return getTypeOfAnyCardinality().collectionSemantics()
                .orElseThrow(()->_Exceptions.unrecoverable(
                        "framework bug: non-scalar %s feature must have a TypeOfFacet",
                        this.getFeatureIdentifier()));
    }

}
