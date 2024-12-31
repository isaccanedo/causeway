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
package org.apache.causeway.core.metamodel.specloader.specimpl;

import org.apache.causeway.applib.Identifier;
import org.apache.causeway.commons.functional.Either;
import org.apache.causeway.core.metamodel.consent.InteractionInitiatedBy;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetedMethod;
import org.apache.causeway.core.metamodel.facets.objectvalue.mandatory.MandatoryFacet;
import org.apache.causeway.core.metamodel.facets.propcoll.accessor.PropertyOrCollectionAccessorFacet;
import org.apache.causeway.core.metamodel.facets.properties.choices.PropertyChoicesFacet;
import org.apache.causeway.core.metamodel.object.ManagedObject;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.causeway.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.causeway.core.metamodel.spec.feature.OneToOneAssociation;

public abstract class ObjectAssociationAbstract
extends ObjectMemberAbstract
implements ObjectAssociation {

    private final ObjectSpecification elementType;

    protected ObjectAssociationAbstract(
            final Identifier featureIdentifier,
            final FacetedMethod facetedMethod,
            final FeatureType featureType,
            final ObjectSpecification elementType) {

        super(featureIdentifier, facetedMethod, featureType);
        if (elementType == null) {
            throw new IllegalArgumentException("field type for '" + getId() + "' must exist");
        }
        this.elementType = elementType;
    }

    @Override
    public final Either<OneToOneAssociation, OneToManyAssociation> getSpecialization() {
        return isSingular()
                ? Either.left((OneToOneAssociation) this)
                : Either.right((OneToManyAssociation) this);
    }

    @Override
    public FacetHolder getFacetHolder() {
        return getFacetedMethod();
    }

    @Override
    public ObjectSpecification getDeclaringType() {
        final PropertyOrCollectionAccessorFacet facet = getFacet(PropertyOrCollectionAccessorFacet.class);
        return facet.getDeclaringType();
    }

    /**
     * Return the specification of the object (or objects) that this field
     * holds. For a value are one-to-one reference this will be type that the
     * accessor returns. For a collection it will be the type of element, not
     * the type of collection.
     */
    @Override
    public ObjectSpecification getElementType() {
        return elementType;
    }

    @Override
    public boolean hasChoices() {
        return containsFacet(PropertyChoicesFacet.class);
    }

    @Override
    public boolean isMandatory() {
        return MandatoryFacet.isMandatory(this);
    }

    @Override
    public abstract boolean isEmpty(final ManagedObject adapter, final InteractionInitiatedBy interactionInitiatedBy);

}
