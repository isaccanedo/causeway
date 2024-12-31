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

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.facets.members.layout.group.LayoutGroupFacetFromPropertyLayoutAnnotation;
import org.apache.causeway.core.metamodel.facets.members.layout.order.LayoutOrderFacetFromPropertyLayoutAnnotation;
import org.apache.causeway.core.metamodel.specloader.validator.ValidationFailureUtils;

public class PropertyLayoutFacetFactory
extends FacetFactoryAbstract {

    @Inject
    public PropertyLayoutFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.PROPERTIES_AND_ACTIONS);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        var facetHolder = processMethodContext.getFacetHolder();
        var propertyLayoutIfAny = processMethodContext
                .synthesizeOnMethodOrMixinType(
                        PropertyLayout.class,
                        () -> ValidationFailureUtils
                        .raiseAmbiguousMixinAnnotations(processMethodContext.getFacetHolder(), PropertyLayout.class));

        addFacetIfPresent(
                CssClassFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                MemberDescribedFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                HiddenFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                LabelAtFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                LayoutGroupFacetFromPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                LayoutOrderFacetFromPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                MultiLineFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                NamedFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(
                PromptStyleFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, getConfiguration(), facetHolder));

        addFacetIfPresent(TypicalLengthFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

        addFacetIfPresent(UnchangingFacetForPropertyLayoutAnnotation
                .create(propertyLayoutIfAny, facetHolder));

    }

}
