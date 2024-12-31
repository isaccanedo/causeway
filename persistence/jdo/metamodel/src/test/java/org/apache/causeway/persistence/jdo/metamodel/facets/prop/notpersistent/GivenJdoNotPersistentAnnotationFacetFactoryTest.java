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
package org.apache.causeway.persistence.jdo.metamodel.facets.prop.notpersistent;

import javax.jdo.annotations.NotPersistent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.causeway.persistence.jdo.metamodel.testing.AbstractFacetFactoryTest;
import org.apache.causeway.persistence.jdo.provider.metamodel.facets.prop.notpersistent.JdoNotPersistentFacet;

class GivenJdoNotPersistentAnnotationFacetFactoryTest
extends AbstractFacetFactoryTest {

    private JdoNotPersistentAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new JdoNotPersistentAnnotationFacetFactory(metaModelContext, jdoFacetContext);
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    public void testFeatureTypes() {
        var featureTypes = facetFactory.getFeatureTypes();
        assertFalse(contains(featureTypes, FeatureType.OBJECT));
        assertTrue(contains(featureTypes, FeatureType.PROPERTY));
        assertFalse(contains(featureTypes, FeatureType.COLLECTION));
        assertFalse(contains(featureTypes, FeatureType.ACTION));
        assertFalse(contains(featureTypes, FeatureType.ACTION_PARAMETER_SINGULAR));
    }

    public void testNotPersistentAnnotationPickedUpOnProperty() throws Exception {
        final Class<?> cls = SimpleObjectWithNotPersistentColumn.class;
        var method = findMethod(cls, "getSomeColumn");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(JdoNotPersistentFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JdoNotPersistentFacet);
    }

    public void testIfNoIdAnnotationThenNoFacet() throws Exception {

        class Customer {
            private Long someColumn;

            // @NotPersistent missing
            @SuppressWarnings("unused")
            public Long getSomeColumn() {
                return someColumn;
            }

            @SuppressWarnings("unused")
            public void setSomeColumn(final Long someColumn) {
                this.someColumn = someColumn;
            }
        }

        final Class<?> cls = Customer.class;
        var method = findMethod(cls, "getSomeColumn");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(JdoNotPersistentFacet.class);
        assertNull(facet);
    }

    public void testNoMethodsRemoved() throws Exception {
        class Customer {
            private Long someColumn;

            @NotPersistent
            public Long getSomeColumn() {
                return someColumn;
            }

            @SuppressWarnings("unused")
            public void setSomeColumn(final Long someColumn) {
                this.someColumn = someColumn;
            }
        }

        final Class<?> cls = Customer.class;
        var method = findMethod(cls, "getSomeColumn");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        assertNoMethodsRemoved();
    }
}
