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
package org.apache.causeway.core.metamodel.facets;

import org.apache.causeway.applib.annotation.Introspection.IntrospectionPolicy;
import org.apache.causeway.commons.internal.exceptions._Exceptions;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;

/**
 * Processes logicalTypeName and determines the effective IntrospectionPolicy.
 */
public interface ObjectTypeFacetFactory extends FacetFactory {

    public static class ProcessObjectTypeContext
    extends AbstractProcessWithClsContext<FacetHolder> {
        public ProcessObjectTypeContext(
                final Class<?> cls,
                final FacetHolder facetHolder) {
            super(cls,
                    IntrospectionPolicy.ANNOTATION_OPTIONAL, // not used - but to satisfy constraints
                    facetHolder);
        }
        @Override
        public IntrospectionPolicy getIntrospectionPolicy() {
            throw _Exceptions.unsupportedOperation(
                    "ProcessObjectTypeContext does not support getIntrospectionPolicy() "
                    + "as the IntrospectionPolicy is not yet available this early "
                    + "in the meta-model processing stage.");
        }
    }

    void process(ProcessObjectTypeContext processClassContext);

}
