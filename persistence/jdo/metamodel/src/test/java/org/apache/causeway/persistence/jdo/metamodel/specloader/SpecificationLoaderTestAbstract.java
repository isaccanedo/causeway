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
package org.apache.causeway.persistence.jdo.metamodel.specloader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.causeway.core.metamodel._testing.MetaModelContext_forTesting;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.apache.causeway.core.metamodel.facets.all.described.MemberDescribedFacet;
import org.apache.causeway.core.metamodel.facets.all.described.ObjectDescribedFacet;
import org.apache.causeway.core.metamodel.facets.all.named.MemberNamedFacet;
import org.apache.causeway.core.metamodel.facets.all.named.ObjectNamedFacet;
import org.apache.causeway.core.metamodel.facets.collections.CollectionFacet;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.specloader.SpecificationLoader;

abstract class SpecificationLoaderTestAbstract {

    protected MetaModelContext metaModelContext;

    // is loaded by subclasses
    protected ObjectSpecification specification;

    @BeforeEach
    public void setUp() throws Exception {

        metaModelContext = MetaModelContext_forTesting.builder()
                .build();

        specification = loadSpecification(metaModelContext.getSpecificationLoader());
    }

    @AfterEach
    public void tearDown() throws Exception {
        metaModelContext.getSpecificationLoader().disposeMetaModel();
    }

    protected abstract ObjectSpecification loadSpecification(SpecificationLoader reflector);

    @Test
    public void testCollectionFacet() throws Exception {
        final Facet facet = specification.getFacet(CollectionFacet.class);
        assertNull(facet);
    }

    @Test
    public void testTypeOfFacet() throws Exception {
        final TypeOfFacet facet = specification.getFacet(TypeOfFacet.class);
        assertNull(facet);
    }

    @Test
    public void testNamedFaced() throws Exception {
        var facet =

                specification.lookupFacet(ObjectNamedFacet.class)
                .map(Facet.class::cast)
                .or(()->specification.lookupFacet(MemberNamedFacet.class))
                .orElse(null);

        assertNotNull(facet);
    }

    @Test @Disabled("we allow descriptions to be absent - no need to install empty fallbacks")
    public void testDescriptionFacet() throws Exception {
        var facet =

                specification.lookupFacet(ObjectDescribedFacet.class)
                .map(Facet.class::cast)
                .or(()->specification.lookupFacet(MemberDescribedFacet.class))
                .orElse(null);

        assertNotNull(facet);
    }

}
