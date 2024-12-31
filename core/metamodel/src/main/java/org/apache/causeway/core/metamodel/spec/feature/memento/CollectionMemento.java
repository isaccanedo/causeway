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
package org.apache.causeway.core.metamodel.spec.feature.memento;

import java.io.Serializable;
import java.util.function.Supplier;

import org.apache.causeway.applib.Identifier;
import org.apache.causeway.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.causeway.core.metamodel.specloader.SpecificationLoader;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;

/**
 * {@link Serializable} representation of an {@link OneToManyAssociation}
 * (a parented collection of entities).
 *
 * @implNote thread-safe memoization
 *
 * @since 2.0 {index}
 */
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionMemento implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Getter private final Identifier identifier;

    // -- FACTORY

    public static CollectionMemento forCollection(final @NonNull OneToManyAssociation collection) {
        return new CollectionMemento(
                collection.getFeatureIdentifier(),
                collection);
    }

    // -- LOAD/UNMARSHAL

    @EqualsAndHashCode.Exclude
    private transient OneToManyAssociation collection;

    @Synchronized
    public OneToManyAssociation getCollection(final @NonNull Supplier<SpecificationLoader> specLoader) {
        if (collection == null) {
            collection = specLoader.get()
                    .specForLogicalTypeElseFail(getIdentifier().getLogicalType())
                    .getCollectionElseFail(getIdentifier().getMemberLogicalName());
        }
        return collection;
    }

    // -- OBJECT CONTRACT

    @Override
    public String toString() {
        return getIdentifier().getLogicalTypeName() + "#" + getIdentifier().getMemberLogicalName();
    }

}
