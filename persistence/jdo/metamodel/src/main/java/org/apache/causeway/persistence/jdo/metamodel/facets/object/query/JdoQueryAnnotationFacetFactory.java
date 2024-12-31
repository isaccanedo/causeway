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
package org.apache.causeway.persistence.jdo.metamodel.facets.object.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facetapi.MetaModelRefiner;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.progmodel.ProgrammingModel;
import org.apache.causeway.core.metamodel.progmodel.ProgrammingModel.Marker;
import org.apache.causeway.persistence.jdo.provider.entities.JdoFacetContext;

public class JdoQueryAnnotationFacetFactory extends FacetFactoryAbstract
implements MetaModelRefiner {

    private final JdoFacetContext jdoFacetContext;

    @Inject
    public JdoQueryAnnotationFacetFactory(
            final MetaModelContext mmc,
            final JdoFacetContext jdoFacetContext) {
        super(mmc, FeatureType.OBJECTS_ONLY);
        this.jdoFacetContext = jdoFacetContext;
    }

    @Override
    public void process(final ProcessClassContext processClassContext) {
        var cls = processClassContext.getCls();

        // only applies to JDO entities; ignore any view models
        if(!jdoFacetContext.isPersistenceEnhanced(cls)) {
            return;
        }

        final Queries namedQueriesAnnotation = processClassContext.synthesizeOnType(Queries.class).orElse(null);
        final FacetHolder facetHolder = processClassContext.getFacetHolder();

        if (namedQueriesAnnotation != null) {
            addFacet(
                    new JdoQueriesFacetAnnotation(namedQueriesAnnotation.value(), facetHolder));
            return;
        }

        final Query namedQueryAnnotation = processClassContext.synthesizeOnType(Query.class).orElse(null);
        if (namedQueryAnnotation != null) {
            addFacet(
                    new JdoQueryFacetAnnotation(namedQueryAnnotation, facetHolder));
        }
    }

    @Override
    public void refineProgrammingModel(final ProgrammingModel programmingModel) {
        var isValidateFromClause =
                getConfiguration().getCore().getMetaModel().getValidator().getJdoql().isFromClause();
        if (isValidateFromClause) {
            programmingModel.addValidator(new MetaModelValidatorForJdoqlFromClause(getMetaModelContext()), Marker.JDO);
        }

        var isValidateVariablesClause =
                getConfiguration().getCore().getMetaModel().getValidator().getJdoql().isVariablesClause();
        if (isValidateVariablesClause) {
            programmingModel.addValidator(new MetaModelValidatorForJdoqlVariablesClause(getMetaModelContext()), Marker.JDO);
        }
    }

    private static final Pattern fromPattern = Pattern.compile("SELECT.*?FROM[\\s]+([^\\s]+).*",
            Pattern.CASE_INSENSITIVE);

    static String from(final String query) {
        final Matcher matcher = fromPattern.matcher(query);
        return matcher.matches() ? matcher.group(1) :  null;
    }

    private static final Pattern variablesPattern = Pattern.compile(".*?VARIABLES[\\s]+([^\\s]+).*",
            Pattern.CASE_INSENSITIVE);

    static String variables(final String query) {
        final Matcher matcher = variablesPattern.matcher(query);
        return matcher.matches() ? matcher.group(1) :  null;
    }

}
