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
package org.apache.causeway.core.metamodel.facets.object.callbacks;

import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.reflection._GenericResolver.ResolvedMethod;
import org.apache.causeway.commons.internal.reflection._MethodFacades.MethodFacade;
import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facetapi.FacetAbstract;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.object.ManagedObject;
import org.apache.causeway.core.metamodel.object.MmInvokeUtils;

import lombok.Getter;

/**
 * Adapter superclass for {@link Facet}s for {@link CallbackFacet}.
 */
public abstract class CallbackFacetAbstract
extends FacetAbstract
implements CallbackFacet {

    @Getter(onMethod_ = {@Override})
    private final Can<MethodFacade> methods;
    private final Can<ResolvedMethod> asRegularMethods;

    protected CallbackFacetAbstract(
            final Class<? extends Facet> facetType,
            final Can<MethodFacade> methods,
            final FacetHolder holder) {
        super(facetType, holder);
        this.methods = methods;
        this.asRegularMethods = methods.map(MethodFacade::asMethodElseFail); // all expected to be regular
    }

    @Override
    public final Intent getIntent() {
        return Intent.LIFECYCLE;
    }

    @Override
    public final void invoke(final ManagedObject adapter) {
        MmInvokeUtils.invokeAll(asRegularMethods.map(ResolvedMethod::method), adapter);
    }

}
