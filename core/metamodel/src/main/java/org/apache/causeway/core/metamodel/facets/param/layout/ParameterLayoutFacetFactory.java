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
package org.apache.causeway.core.metamodel.facets.param.layout;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;

public class ParameterLayoutFacetFactory
extends FacetFactoryAbstract {

    @Inject
    public ParameterLayoutFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.PARAMETERS_ONLY);
    }

    @Override
    public void processParams(final ProcessParameterContext processParameterContext) {
        var parameterLayoutIfAny = processParameterContext.synthesizeOnParameter(ParameterLayout.class);
        addFacets(processParameterContext, parameterLayoutIfAny);
    }

    protected void addFacets(
            final ProcessParameterContext processParameterContext,
            final Optional<ParameterLayout> parameterLayoutIfAny) {

        var facetHolder = processParameterContext.getFacetHolder();

        addFacetIfPresent(
                CssClassFacetForParameterLayoutAnnotation
                .create(parameterLayoutIfAny, facetHolder));

        addFacetIfPresent(
                ParamDescribedFacetForParameterLayoutAnnotation
                .create(parameterLayoutIfAny, facetHolder));

        addFacetIfPresent(
                LabelAtFacetForParameterLayoutAnnotation
                .create(parameterLayoutIfAny, facetHolder));

        addFacetIfPresent(
                MultiLineFacetForParameterLayoutAnnotation
                .create(parameterLayoutIfAny, facetHolder));

        addFacetIfPresent(
                NamedFacetForParameterLayoutAnnotation
                .create(parameterLayoutIfAny, facetHolder));

        addFacetIfPresent(TypicalLengthFacetForParameterLayoutAnnotation
                .create(parameterLayoutIfAny, facetHolder));

    }

}
