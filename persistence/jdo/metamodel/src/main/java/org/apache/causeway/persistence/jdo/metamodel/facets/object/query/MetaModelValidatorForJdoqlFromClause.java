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

import java.util.Objects;

import org.apache.causeway.applib.Identifier;
import org.apache.causeway.commons.functional.Try;
import org.apache.causeway.commons.internal.context._Context;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.spec.Hierarchical;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.specloader.validator.ValidationFailure;

class MetaModelValidatorForJdoqlFromClause
extends MetaModelValidatorForJdoqlAbstract {

    MetaModelValidatorForJdoqlFromClause(final MetaModelContext mmc) {
        super(mmc, "FROM");
    }

    @Override
    String deriveClause(final String query) {
        return JdoQueryAnnotationFacetFactory.from(query);
    }

    @Override
    void postInterpretJdoql(
            final String classNameFromClause, // actually class not logical type!
            final ObjectSpecification objectSpec,
            final String query) {

        var className = objectSpec.getCorrespondingClass().getName();
        if (Objects.equals(classNameFromClause, className)) {
            return;
        }

        var fromSpecResult = Try.call(()->getSpecificationLoader()
                    .specForType(_Context.loadClass(classNameFromClause))
                    .orElse(null));

        if(!fromSpecResult.getValue().isPresent()) {
            ValidationFailure.raise(
                    objectSpec.getSpecificationLoader(),
                    Identifier.classIdentifier(objectSpec.getLogicalType()),
                    String.format(
                            "%s: error in JDOQL query, "
                            + "logical type name after '%s' clause could not be loaded (JDOQL : %s)",
                            className,
                            clause,
                            query)
                    );
            return;
        }

        var fromSpec = fromSpecResult.getValue().get();
        var subclasses = fromSpec.subclasses(Hierarchical.Depth.TRANSITIVE);
        if(subclasses.contains(objectSpec)) {
            return;
        }

        ValidationFailure.raise(
                objectSpec.getSpecificationLoader(),
                Identifier.classIdentifier(objectSpec.getLogicalType()),
                String.format(
                        "%s: error in JDOQL query, type name after '%s' "
                        + "clause should be same as class name on which annotated, "
                        + "or one of its supertypes (JDOQL : %s)",
                        className,
                        clause,
                        query)
                );
    }

}
