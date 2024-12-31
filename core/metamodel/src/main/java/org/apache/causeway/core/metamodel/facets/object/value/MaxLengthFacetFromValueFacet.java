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
package org.apache.causeway.core.metamodel.facets.object.value;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.causeway.applib.value.semantics.Parser;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.objectvalue.maxlen.MaxLengthFacet;
import org.apache.causeway.core.metamodel.facets.objectvalue.maxlen.MaxLengthFacetAbstract;

public class MaxLengthFacetFromValueFacet
extends MaxLengthFacetAbstract{

    private final Parser<?> parser;

    public static Optional<MaxLengthFacet> create(final ValueFacet<?> valueFacet, final FacetHolder holder) {
        return valueFacet.selectDefaultParser()
                .filter(parser->parser.maxLength()>=0)
                .map(parser->new MaxLengthFacetFromValueFacet(parser, holder));
    }

    // -- CONSTRUCTION

    private MaxLengthFacetFromValueFacet(final Parser<?> parser, final FacetHolder holder) {
        super(parser.maxLength(), holder);
        this.parser = parser;
    }

    @Override
    public void visitAttributes(final BiConsumer<String, Object> visitor) {
        super.visitAttributes(visitor);
        visitor.accept("parser", parser.toString());
    }

    @Override
    public String toString() {
        return "maxLength=" + value();
    }

}
