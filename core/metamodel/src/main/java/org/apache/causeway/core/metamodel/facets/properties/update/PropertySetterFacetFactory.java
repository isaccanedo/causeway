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
package org.apache.causeway.core.metamodel.facets.properties.update;

import javax.inject.Inject;

import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.commons.semantics.AccessorSemantics;
import org.apache.causeway.core.config.progmodel.ProgrammingModelConstants.ReturnTypeCategory;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.properties.update.clear.PropertyClearFacetViaSetterMethod;
import org.apache.causeway.core.metamodel.facets.properties.update.init.PropertyInitializationFacetViaSetterMethod;
import org.apache.causeway.core.metamodel.facets.properties.update.modify.PropertySetterFacetViaSetterMethod;
import org.apache.causeway.core.metamodel.methods.MethodFinder;
import org.apache.causeway.core.metamodel.methods.MethodPrefixBasedFacetFactoryAbstract;

/**
 * Sets up the {@link PropertySetterFacetViaSetterMethod} to invoke the
 * property's setter if available, but if none then marks the property as
 * {@link SnapshotExcludeFacetFromImmutableMember not-persistable}.
 */
public class PropertySetterFacetFactory
extends MethodPrefixBasedFacetFactoryAbstract {

    @Inject
    public PropertySetterFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.PROPERTIES_ONLY, OrphanValidation.VALIDATE, Can.empty());
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        var getterMethod = processMethodContext.getMethod();
        final String capitalizedName = _Strings.baseName(getterMethod.getName());
        var methodNameCandidates = Can.ofSingleton(
                AccessorSemantics.SET.prefix(capitalizedName));

        final Class<?>[] signature = new Class[] { getterMethod.getReturnType() };

        var setterMethods =
        MethodFinder
        .accessor(processMethodContext.getCls(), methodNameCandidates, processMethodContext.getIntrospectionPolicy())
        .withReturnTypeAnyOf(ReturnTypeCategory.VOID.getReturnTypes())
        .streamMethodsMatchingSignature(signature)
        .peek(processMethodContext::removeMethod)
        .collect(Can.toCan());

        final FacetHolder property = processMethodContext.getFacetHolder();
        if (setterMethods.isNotEmpty()) {

            setterMethods
            .forEach(setterMethod->{
                addFacet(new PropertySetterFacetViaSetterMethod(setterMethod, property));
                addFacet(new PropertyInitializationFacetViaSetterMethod(setterMethod, property));
                addFacet(new PropertyClearFacetViaSetterMethod(setterMethod, property));
            });

        } else {
            addFacet(new SnapshotExcludeFacetFromImmutableMember(property));

            // previously we also added the DisabledFacetAlwaysEverywhere facet here.
            // however, the PropertyModifyFacetFactory (which comes next) might install a PropertySetterFacet instead.
            // so, have introduced a new facet factory, to be run "near the end", to install this facet if no
            // setter facet is found to have been installed.

        }

    }

}
