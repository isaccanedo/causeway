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
package org.apache.causeway.core.metamodel.facets.collections.sortedby.annotation;

import java.util.Comparator;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facetapi.MetaModelRefiner;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.facets.collections.sortedby.SortedByFacet;
import org.apache.causeway.core.metamodel.progmodel.ProgrammingModel;
import org.apache.causeway.core.metamodel.spec.feature.MixedIn;
import org.apache.causeway.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.causeway.core.metamodel.specloader.validator.ValidationFailure;

/**
 * There is no check that the value is a {@link Comparator};
 * instead this is done via {@link #refineProgrammingModel(ProgrammingModel)}.
 */
public class SortedByFacetAnnotationFactory
extends FacetFactoryAbstract
implements MetaModelRefiner {

    @Inject
    public SortedByFacetAnnotationFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.COLLECTIONS_ONLY);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {
        // previously this handled the (now deleted) @SortedBy annotation.
        // nothing now to do here, but there is still validation to be contributed.
        // TODO: move this validator to a different facet factory, eg @CollectionLayout facet factory.

    }

    @Override
    public void refineProgrammingModel(final ProgrammingModel programmingModel) {
        programmingModel.addValidatorSkipManagedBeans(objectSpec->{
            final Stream<OneToManyAssociation> objectCollections =
                    objectSpec.streamCollections(MixedIn.EXCLUDED);

            objectCollections.forEach(objectCollection->{
                final SortedByFacet facet = objectCollection.getFacet(SortedByFacet.class);
                if(facet != null) {
                    final Class<? extends Comparator<?>> cls = facet.value();
                    if(!Comparator.class.isAssignableFrom(cls)) {

                        ValidationFailure.raiseFormatted(
                                objectSpec,
                                String.format(
                                    "%s#%s: is annotated with @SortedBy, "
                                    + "but the class specified '%s' is not a Comparator",
                                    objectSpec.getFeatureIdentifier().getClassName(),
                                    objectCollection.getId(),
                                    facet.value().getName()));
                    }
                }
            });
        });
    }

}
