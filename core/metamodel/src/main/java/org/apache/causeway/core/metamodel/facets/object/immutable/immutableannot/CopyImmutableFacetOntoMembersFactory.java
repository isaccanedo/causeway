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
package org.apache.causeway.core.metamodel.facets.object.immutable.immutableannot;

import javax.inject.Inject;

import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FacetUtil;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.facets.FacetedMethod;
import org.apache.causeway.core.metamodel.facets.object.immutable.ImmutableFacet;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;

public class CopyImmutableFacetOntoMembersFactory extends FacetFactoryAbstract {

    @Inject
    public CopyImmutableFacetOntoMembersFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.MEMBERS);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {
        final FacetedMethod member = processMethodContext.getFacetHolder();
        final Class<?> owningClass = processMethodContext.getCls();
        final ObjectSpecification owningSpec = getSpecificationLoader().loadSpecification(owningClass);

        // assuming, that immutability is an object-type level concern and not a member-type level concern
        // it is save to just copy onto members, as ImmutableFacet(s) should never ever be declared
        // on members individually (such that there cannot be any conflicts while doing this copies)
        owningSpec
            .lookupFacet(ImmutableFacet.class)
            .ifPresent(immutableFacet->
                FacetUtil.addFacet(immutableFacet.clone(member)));
    }

}
