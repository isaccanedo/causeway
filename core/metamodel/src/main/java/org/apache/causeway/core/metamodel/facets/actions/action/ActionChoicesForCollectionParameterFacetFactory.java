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
package org.apache.causeway.core.metamodel.facets.actions.action;

import javax.inject.Inject;

import org.apache.causeway.commons.internal.exceptions._Exceptions;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facetapi.MetaModelRefiner;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.apache.causeway.core.metamodel.facets.object.autocomplete.AutoCompleteFacet;
import org.apache.causeway.core.metamodel.facets.param.autocomplete.ActionParameterAutoCompleteFacet;
import org.apache.causeway.core.metamodel.facets.param.choices.ActionParameterChoicesFacet;
import org.apache.causeway.core.metamodel.progmodel.ProgrammingModel;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.spec.feature.MixedIn;
import org.apache.causeway.core.metamodel.spec.feature.ObjectAction;
import org.apache.causeway.core.metamodel.spec.feature.ObjectActionParameter;
import org.apache.causeway.core.metamodel.specloader.specimpl.ObjectActionMixedIn;
import org.apache.causeway.core.metamodel.specloader.validator.ValidationFailure;

/**
 * Ensures that every action that has a collection parameter has a choices facet for that parameter.
 */
public class ActionChoicesForCollectionParameterFacetFactory
extends FacetFactoryAbstract
implements MetaModelRefiner {

    @Inject
    public ActionChoicesForCollectionParameterFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.ACTIONS_ONLY);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        // no-op here... just adding a validator

    }

    @Override
    public void refineProgrammingModel(final ProgrammingModel programmingModel) {

        var shouldCheck = getConfiguration().getCore().getMetaModel().getValidator().isActionCollectionParameterChoices();
        if(!shouldCheck) {
            return;
        }

        programmingModel.addValidatorSkipAbstract(objectSpec->{
            objectSpec.streamAnyActions(MixedIn.INCLUDED)
            .forEach(objectAction->{
                if(objectAction instanceof ObjectActionMixedIn) {
                    // we'll report only the mixin
                    return;
                }

                int paramNum = 0;
                for (ObjectActionParameter parameter : objectAction.getParameters()) {
                    if(parameter.getFeatureType() == FeatureType.ACTION_PARAMETER_PLURAL) {
                        validateActionParameter_whenCollection(
                                objectSpec, objectAction, parameter, paramNum);
                    }
                    paramNum++;
                }
            });
        });

    }

    private static void validateActionParameter_whenCollection(
            final ObjectSpecification objectSpec,
            final ObjectAction objectAction,
            final ObjectActionParameter parameter,
            final int paramNum) {

        if(parameter.isSingular()) {
            return;
        }

        parameter.lookupFacet(TypeOfFacet.class)
        .ifPresentOrElse(typeOfFacet->{

            // Violation if there are action parameter types that are assignable
            // from java.util.Collection but are not of
            // exact type List, Set, SortedSet or Collection.
            if(!typeOfFacet.value().isSupportedForActionParameter()) {

                var messageFormat = "Collection action parameter found that is not exactly one "
                        + "of the following supported types: "
                        + "List, Set, SortedSet, Collection, Can or Array.  "
                        + "Class: %s action: %s parameter %d";

                ValidationFailure.raise(
                        objectSpec,
                        String.format(
                                messageFormat,
                                objectSpec.getFullIdentifier(),
                                objectAction.getId(),
                                paramNum));
            }

        },()->{

            var messageFormat = "framework bug: non-scalar action parameter found,"
                    + " that has no TypeOfFacet"
                    + "Class: %s action: %s parameter %d";

            throw _Exceptions.unrecoverable(
                            messageFormat,
                            objectSpec.getFullIdentifier(),
                            objectAction.getId(),
                            paramNum);

        });

        var parameterTypeSpec = parameter.getElementType();

        if (parameter.containsFacet(ActionParameterChoicesFacet.class)
                || parameter.containsFacet(ActionParameterAutoCompleteFacet.class)
                || parameterTypeSpec.containsNonFallbackFacet(AutoCompleteFacet.class)) {
            return;
        }

        var messageFormat = "Collection action parameter found without supporting "
                + "choices or autoComplete facet.  "
                + "Class: %s action: %s parameter %d";

        ValidationFailure.raise(
                objectSpec,
                String.format(
                        messageFormat,
                        objectSpec.getFullIdentifier(),
                        objectAction.getId(),
                        paramNum));
    }

}
