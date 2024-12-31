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
package org.apache.causeway.core.metamodel.facets.object.ignore.javalang;

import java.lang.reflect.Method;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.causeway.commons.functional.Try;
import org.apache.causeway.commons.internal._Constants;
import org.apache.causeway.commons.internal.reflection._GenericResolver.ResolvedMethod;
import org.apache.causeway.commons.internal.reflection._Reflect;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.methods.MethodFilteringFacetFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Designed to simply filter out {@link Iterable#iterator()} method if it
 * exists.
 * <p>
 * Does not add any {@link Facet}s.
 */
public class IteratorFilteringFacetFactory
extends FacetFactoryAbstract
implements MethodFilteringFacetFactory, Iterable<Object> {

    @Inject
    public IteratorFilteringFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.OBJECTS_ONLY);
    }

    @Override
    public void process(final ProcessClassContext processClassContext) {
        processClassContext.removeMethod("iterator", java.util.Iterator.class, _Constants.emptyClasses);
    }

    @Override
    public boolean recognizes(final ResolvedMethod method) {
        return _Reflect.methodsSame(iteratorMethod(), method.method());
    }

    // -- HELPER

    // implemented, so we can get a Method reference below
    @Override
    public Iterator<Object> iterator() {
        return null;
    }

    @Getter(lazy = true, value = AccessLevel.PRIVATE) @Accessors(fluent = true)
    private final Method iteratorMethod = Try.call(()->getClass().getMethod("iterator"))
        .valueAsNonNullElseFail();

}
