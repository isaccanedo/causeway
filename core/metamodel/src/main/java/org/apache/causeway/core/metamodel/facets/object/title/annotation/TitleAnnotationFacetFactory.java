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
package org.apache.causeway.core.metamodel.facets.object.title.annotation;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facetapi.MetaModelRefiner;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.facets.fallback.FallbackFacetFactory;
import org.apache.causeway.core.metamodel.facets.object.title.TitleFacet;
import org.apache.causeway.core.metamodel.facets.object.title.methods.TitleFacetViaTitleMethod;
import org.apache.causeway.core.metamodel.progmodel.ProgrammingModel;
import org.apache.causeway.core.metamodel.specloader.validator.ValidationFailure;

public class TitleAnnotationFacetFactory
extends FacetFactoryAbstract
implements MetaModelRefiner {

    @Inject
    public TitleAnnotationFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.OBJECTS_ONLY);
    }

    /**
     * If no method tagged with {@link Title} annotation then will use Facets
     * provided by {@link FallbackFacetFactory} instead.
     */
    @Override
    public void process(final ProcessClassContext processClassContext) {
        var cls = processClassContext.getCls();
        var facetHolder = processClassContext.getFacetHolder();

        addFacetIfPresent(TitleFacetViaTitleAnnotation.create(cls, facetHolder));
    }

    /**
     * Violation if there is a class that has both a <tt>title()</tt> method
     * and also any declared (non-inherited) method annotated with <tt>@Title</tt>.
     * <p>
     * If there are only inherited methods annotated with <tt>@Title</tt>
     * then this is <i>not</i> a violation;
     * but (from the {@link TitleFacetViaTitleMethod}
     * the imperative <tt>title()</tt> method will take
     * precedence.
     */
    @Override
    public void refineProgrammingModel(final ProgrammingModel programmingModel) {

        programmingModel.addValidatorSkipManagedBeans(objectSpec -> {

            var titleFacetTopRank =
                objectSpec
                .getFacetRanking(TitleFacet.class)
                .map(facetRanking->facetRanking.getTopRank(TitleFacet.class))
                .orElse(Can.empty())
                .distinct(TitleFacet::semanticEquals);

            // top-rank if present must not be ambiguous
            if(titleFacetTopRank.isCardinalityMultiple()) {

                var conflictingFeatures =
                        titleFacetTopRank
                        .map(TitleFacet::getClass)
                        .map(Class::getSimpleName)
                        .toList();

                ValidationFailure.raiseFormatted(
                        objectSpec,
                        "%s: conflict for determining a strategy for retrieval of title for class, "
                        + "conflicting title facets %s",
                        objectSpec.getFeatureIdentifier().getClassName(),
                        conflictingFeatures.toString());
            }

        });
    }

}
