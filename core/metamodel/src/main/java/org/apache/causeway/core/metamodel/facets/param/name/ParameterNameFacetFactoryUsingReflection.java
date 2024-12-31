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
package org.apache.causeway.core.metamodel.facets.param.name;

import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FacetUtil;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;

/**
 * Uses JDK8+ reflection API to derive the parameter name from the code.
 * <p>
 * For Java code compiled with {@code javac} requires to be compiled with the
 * {@code -parameters} flag.
 * <p>
 * For Java code compiled with Eclipse requires to be compiled with the
 * {@code Preferences>Java>Compiler} ... {@code Store information about method parameters}
 * flag set.
 *
 * @since 2.0
 */
public class ParameterNameFacetFactoryUsingReflection
extends FacetFactoryAbstract {

    private final Pattern argXPattern = Pattern.compile("arg\\d+");

    @Inject
    public ParameterNameFacetFactoryUsingReflection(final MetaModelContext mmc) {
        super(mmc, FeatureType.PARAMETERS_ONLY);
    }

    @Override
    public void processParams(final ProcessParameterContext processParameterContext) {

        var parameterName = processParameterContext.getParameterName();

        // if not compiled with -parameters flag or synthetic, then ignore
        var argXMatcher = argXPattern.matcher(parameterName);
        if (argXMatcher.matches()){
            return;
        }

        var naturalName = _Strings.asNaturalName.apply(parameterName);
        var facetHolder = processParameterContext.getFacetHolder();

        FacetUtil.addFacet(
                new NamedFacetForParameterUsingReflection(naturalName, facetHolder));
    }

}
