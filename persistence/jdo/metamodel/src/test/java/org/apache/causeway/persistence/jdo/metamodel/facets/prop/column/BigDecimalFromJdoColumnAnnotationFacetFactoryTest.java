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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.core.metamodel._testing.MetaModelContext_forTesting;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.causeway.core.metamodel.facets.FacetedMethod;
import org.apache.causeway.core.metamodel.facets.objectvalue.digits.MaxFractionalDigitsFacet;
import org.apache.causeway.core.metamodel.facets.objectvalue.digits.MaxTotalDigitsFacet;
import org.apache.causeway.persistence.jdo.metamodel.testing.AbstractFacetFactoryTest;

class BigDecimalFromJdoColumnAnnotationFacetFactoryTest
extends AbstractFacetFactoryTest {

    private BigDecimalFromJdoColumnAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        var mmc = MetaModelContext_forTesting.buildDefault();
        facetFactory = new BigDecimalFromJdoColumnAnnotationFacetFactory(mmc);
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

    public void testAnnotationPickedUpOnProperty() throws Exception {
        final Class<?> cls = SimpleObjectWithBigDecimalColumnAnnotations.class;
        var method = findMethod(cls, "getBigDecimalPropertyWithColumnAnnotation");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        assertBigDecimalSemantics(facetedMethod, 12, 3);
    }

    public void testAnnotationDefaultsLengthIfMissing() throws Exception {
        final Class<?> cls = SimpleObjectWithBigDecimalColumnAnnotations.class;
        var method = findMethod(cls, "getBigDecimalPropertyWithColumnAnnotationMissingLength");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        assertBigDecimalSemantics(facetedMethod, -1, 3);
    }

    public void testAnnotationDefaultsScaleIfMissing() throws Exception {
        final Class<?> cls = SimpleObjectWithBigDecimalColumnAnnotations.class;
        var method = findMethod(cls, "getBigDecimalPropertyWithColumnAnnotationMissingScale");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        assertBigDecimalSemantics(facetedMethod, 12, -1);
    }

    public void testNoFacetIfPropertyTypeIsNotBigDecimal() throws Exception {
        final Class<?> cls = SimpleObjectWithBigDecimalColumnAnnotations.class;
        var method = findMethod(cls, "getStringPropertyWithColumnAnnotation");
        facetFactory.process(ProcessMethodContext
                .forTesting(cls, null, method, methodRemover, facetedMethod));

        assertBigDecimalSemantics(facetedMethod, -1, -1);
    }

    // -- HELPER

    private void assertBigDecimalSemantics(
            final FacetedMethod facetedMethod, final int maxTotalDigits, final int maxFractionalDigits) {
        if(maxTotalDigits>=0) {
            final MaxTotalDigitsFacet facet = facetedMethod.getFacet(MaxTotalDigitsFacet.class);
            assertNotNull(facet);
            assertTrue(facet instanceof MaxTotalDigitsFacetFromJdoColumnAnnotation);
            assertThat(facet.getMaxTotalDigits(), is(maxTotalDigits));
        } else {
            assertNull(facetedMethod.getFacet(MaxTotalDigitsFacet.class));
        }

        if(maxFractionalDigits>=0) {
            final MaxFractionalDigitsFacet facet = facetedMethod.getFacet(MaxFractionalDigitsFacet.class);
            assertNotNull(facet);
            assertTrue(facet instanceof MaxFractionalDigitsFacetFromJdoColumnAnnotation);
            assertThat(facet.getMaxFractionalDigits(), is(maxFractionalDigits));
        } else {
            assertNull(facetedMethod.getFacet(MaxFractionalDigitsFacet.class));
        }
    }

}
