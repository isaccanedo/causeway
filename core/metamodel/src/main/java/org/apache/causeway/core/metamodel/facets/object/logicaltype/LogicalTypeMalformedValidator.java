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
package org.apache.causeway.core.metamodel.facets.object.logicaltype;

import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.core.config.progmodel.ProgrammingModelConstants;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.specloader.validator.MetaModelValidatorAbstract;
import org.apache.causeway.core.metamodel.specloader.validator.ValidationFailure;

/**
 * DomainObjects must have a non-empty namespace,
 * eg. @DomainObject(logicalTypeName="Customer") is considered invalid,
 * whereas eg. @DomainObject(logicalTypeName="sales.Customer") is valid.
 *
 * @since 2.0
 */
public class LogicalTypeMalformedValidator
extends MetaModelValidatorAbstract {

    public LogicalTypeMalformedValidator(final MetaModelContext metaModelContext) {
        super(metaModelContext, spec->spec.isEntityOrViewModel()
                || spec.isInjectable());
    }

    @Override
    public void validateObjectEnter(final ObjectSpecification spec) {

        var logicalType = spec.getLogicalType();
        var logicalTypeName = logicalType.getLogicalTypeName();

        var nameParts = _Strings.splitThenStream(logicalTypeName, ".")
                .collect(Can.toCan());

        if(!nameParts.getCardinality().isMultiple()
                || nameParts.stream()
                    .anyMatch(String::isEmpty)) {

            var validationResponse = spec.isInjectable()
                    ? ProgrammingModelConstants.MessageTemplate.DOMAIN_SERVICE_MISSING_A_NAMESPACE
                    : ProgrammingModelConstants.MessageTemplate.DOMAIN_OBJECT_MISSING_A_NAMESPACE;

            ValidationFailure.raiseFormatted(spec,
                    validationResponse.builder()
                        .addVariable("type", spec.getFullIdentifier())
                        .addVariable("logicalTypeName", logicalTypeName)
                        .buildMessage());
        }

    }

}
