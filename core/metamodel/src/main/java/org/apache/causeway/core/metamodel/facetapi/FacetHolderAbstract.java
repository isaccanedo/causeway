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
package org.apache.causeway.core.metamodel.facetapi;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.causeway.applib.Identifier;
import org.apache.causeway.commons.internal.base._Lazy;
import org.apache.causeway.commons.internal.collections._Maps;
import org.apache.causeway.core.metamodel.context.MetaModelContext;

import static org.apache.causeway.commons.internal.base._Casts.uncheckedCast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * For base subclasses or, more likely, to help write tests.
 */
@AllArgsConstructor
@RequiredArgsConstructor
//@Log4j2
abstract class FacetHolderAbstract
implements FacetHolder {

    // -- FIELDS

    @Getter(onMethod_ = {@Override}) private final @NonNull MetaModelContext metaModelContext;

    // not private nor final, as featureIdentifier might depend on lazily provided LogicalTypeFacet
    @Getter(onMethod_ = {@Override}) protected Identifier featureIdentifier;

    private final Map<Class<? extends Facet>, FacetRanking> rankingByType = _Maps.newHashMap();
    private final Object $lock = new Object();

    @Override
    public final boolean containsFacet(final Class<? extends Facet> facetType) {
        synchronized($lock) {
            return snapshot.get().containsKey(facetType);
        }
    }

    @Override
    public final void addFacet(final @NonNull Facet facet) {
        synchronized($lock) {

            var ranking = rankingByType.computeIfAbsent(facet.facetType(), FacetRanking::new);
            var needsInvalidate = ranking.add(facet);
            if(needsInvalidate) {
                snapshot.clear(); //invalidate
            }
        }
    }

    //TODO should be made final - however, overridden by ObjectSpecificationAbstract,
    // which potentially leads to inconsistent behavior with facet and facet-ranking streaming
    @Override
    public /*final*/ <T extends Facet> T getFacet(final Class<T> facetType) {
        synchronized($lock) {
            return uncheckedCast(snapshot.get().get(facetType));
        }
    }

    @Override
    public final Stream<Facet> streamFacets() {
        synchronized($lock) {
            // consumers should play nice and don't take too long (as we have a lock)
            return snapshot.get().values().stream();
        }
    }

    @Override
    public final int getFacetCount() {
        synchronized($lock) {
            return snapshot.get().size();
        }
    }

    // -- VALIDATION SUPPORT

    @Override
    public final Stream<FacetRanking> streamFacetRankings() {
        return rankingByType.values().stream();
    }

    @Override
    public final Optional<FacetRanking> getFacetRanking(final Class<? extends Facet> facetType) {
        return Optional.ofNullable(rankingByType.get(facetType));
    }

    // -- HELPER

    private final _Lazy<Map<Class<? extends Facet>, Facet>> snapshot = _Lazy.threadSafe(this::snapshot);

    // collect all facet information provided with the top-level facets (contributed facets and aliases)
    private Map<Class<? extends Facet>, Facet> snapshot() {
        var snapshot = _Maps.<Class<? extends Facet>, Facet>newHashMap();
        rankingByType.values()
        .stream()
        .map(facetRanking->facetRanking.getWinner(facetRanking.facetType()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(winningFacet->{

            snapshot.put(
                    winningFacet.facetType(),
                    winningFacet);

        });
        return snapshot;
    }

}
