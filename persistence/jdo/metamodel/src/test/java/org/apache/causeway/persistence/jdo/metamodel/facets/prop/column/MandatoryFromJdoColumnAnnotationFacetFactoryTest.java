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
package org.apache.causeway.persistence.jdo.metamodel.facets.prop.column;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.causeway.core.metamodel.facets.objectvalue.mandatory.MandatoryFacet;
import org.apache.causeway.persistence.jdo.metamodel.testing.AbstractFacetFactoryTest;

class MandatoryFromJdoColumnAnnotationFacetFactoryTest
extends AbstractFacetFactoryTest {

    private MandatoryFromJdoColumnAnnotationFacetFactory facetFactory;
    private Class<?> cls;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new MandatoryFromJdoColumnAnnotationFacetFactory(metaModelContext, jdoFacetContext);
        cls = SimpleObjectWithColumnAllowsNullAnnotations.class;
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

    public void testPrimitiveWithNoAnnotation_isMandatory() throws Exception {
        var method = findMethod(cls, "getPrimitiveWithNoAnnotation");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromAbsenceOfJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(false));
    }

    public void testPrimitiveWithNoAllowsNull_isMandatory() throws Exception {
        var method = findMethod(cls, "getPrimitiveWithNoAllowsNull");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(false));
    }

    public void testPrimitiveWithAllowsNullFalse() throws Exception {
        var method = findMethod(cls, "getPrimitiveWithAllowsNullFalse");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(false));
    }

    public void testPrimitiveWithAllowsNullTrue() throws Exception {
        var method = findMethod(cls, "getPrimitiveWithAllowsNullTrue");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(true));
    }

    public void testReferenceWithNoAnnotation_isOptional() throws Exception {
        var method = findMethod(cls, "getReferenceWithNoAnnotation");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromAbsenceOfJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(true));
    }

    public void testReferenceWithNoAllowsNull_isOptional() throws Exception {
        var method = findMethod(cls, "getReferenceWithNoAllowsNull");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(true));
    }

    public void testReferenceWithAllowsNullFalse() throws Exception {
        var method = findMethod(cls, "getReferenceWithAllowsNullFalse");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(false));
    }

    public void testReferenceWithAllowsNullTrue() throws Exception {
        var method = findMethod(cls, "getReferenceWithAllowsNullTrue");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        final MandatoryFacet facet = facetedMethod.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetFromJdoColumnAnnotation);
        assertThat(facet.getSemantics().isOptional(), is(true));
    }

}
