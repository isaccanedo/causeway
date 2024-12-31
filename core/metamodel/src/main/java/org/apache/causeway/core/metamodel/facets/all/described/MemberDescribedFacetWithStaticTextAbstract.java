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
package org.apache.causeway.core.metamodel.facets.all.described;

import org.apache.causeway.commons.functional.Either;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.all.i8n.imperative.HasImperativeText;
import org.apache.causeway.core.metamodel.facets.all.i8n.staatic.HasStaticText;
import org.apache.causeway.core.metamodel.facets.all.i8n.staatic.HasStaticTextFacetAbstract;

import lombok.Getter;

/**
 * One of two bases for the {@link MemberDescribedFacet}.
 *
 * @see MemberDescribedFacetWithImperativeTextAbstract
 * @since 2.0
 */
public abstract class MemberDescribedFacetWithStaticTextAbstract
extends HasStaticTextFacetAbstract
implements MemberDescribedFacet {

    public static final Class<MemberDescribedFacet> type() {
        return MemberDescribedFacet.class;
    }

    @Getter(onMethod_ = {@Override})
    private final Either<HasStaticText, HasImperativeText> specialization = Either.left(this);

    protected MemberDescribedFacetWithStaticTextAbstract(
            final String originalText,
            final FacetHolder holder) {
        this(
                originalText,
                holder,
                Precedence.DEFAULT);
    }

    protected MemberDescribedFacetWithStaticTextAbstract(
            final String originalText,
            final FacetHolder holder,
            final Precedence precedence) {
        super(type(),
                holder.getTranslationContext(),
                originalText,
                holder,
                precedence);
    }

}
