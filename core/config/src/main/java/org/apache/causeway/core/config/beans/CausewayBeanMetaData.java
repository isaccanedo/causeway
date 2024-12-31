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
package org.apache.causeway.core.config.beans;

import java.util.Optional;

import org.apache.causeway.applib.id.LogicalType;
import org.apache.causeway.applib.services.metamodel.BeanSort;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class CausewayBeanMetaData {

    public enum ManagedBy {
        NONE,
        CAUSEWAY,
        SPRING,
        /** other Spring managed component, or not managed at all */
        INDIFFERENT,
        ;
        public boolean isNone() { return this == NONE; }
        public boolean isCauseway() { return this == CAUSEWAY; }
        public boolean isSpring() { return this == SPRING; }

        /**
         * Whether Spring should make that underlying bean injectable.
         * @implNote if not managed by Causeway, let ultimately Spring decide
         */
        public boolean isVetoedForInjection() {
            return isCauseway()
                    || isNone();
        }
        /**
         * Whether we interfere with Spring's naming strategy.
         */
        public boolean isBeanNameOverride() {
            return this == CAUSEWAY;
        }
    }

    private final @NonNull BeanSort beanSort;
    /**
     * Optionally the {@link PersistenceStack},
     * based on whether {@link #getBeanSort()} is {@link BeanSort#ENTITY}.
     */
    private final @NonNull Optional<PersistenceStack> persistenceStack;
    private final @NonNull LogicalType logicalType;
    private @NonNull ManagedBy managedBy;

    public Class<?> getCorrespondingClass() {
        return logicalType.getCorrespondingClass();
    }

    public String getBeanName() {
        return logicalType.getLogicalTypeName();
    }

    // -- FACTORIES

    public static CausewayBeanMetaData notManaged(
            final @NonNull BeanSort beanSort,
            final @NonNull LogicalType logicalType) {
        return of(beanSort, Optional.empty(), logicalType, ManagedBy.NONE);
    }

    public static CausewayBeanMetaData notManaged(
            final @NonNull BeanSort beanSort,
            final @NonNull Class<?> type) {
        return notManaged(beanSort, LogicalType.infer(type));
    }

    public static CausewayBeanMetaData injectable(
            final @NonNull BeanSort beanSort,
            final @NonNull LogicalType logicalType) {
        return of(beanSort, Optional.empty(), logicalType, ManagedBy.SPRING);
    }

    /**
     * Let <i>Spring</i> decide.
     */
    public static CausewayBeanMetaData indifferent(
            final @NonNull BeanSort beanSort,
            final @NonNull Class<?> type) {
        return of(beanSort, Optional.empty(), LogicalType.infer(type),
                ManagedBy.INDIFFERENT);
    }

    public static CausewayBeanMetaData entity(
            final @NonNull PersistenceStack persistenceStack,
            final @NonNull LogicalType logicalType) {
        return of(BeanSort.ENTITY, Optional.of(persistenceStack), logicalType, ManagedBy.CAUSEWAY);
    }

    public static CausewayBeanMetaData causewayManaged(
            final @NonNull BeanSort beanSort,
            final @NonNull LogicalType logicalType) {
        return of(beanSort, Optional.empty(), logicalType, ManagedBy.CAUSEWAY);
    }

    public static CausewayBeanMetaData causewayManaged(
            final @NonNull BeanSort beanSort,
            final @NonNull Class<?> type) {
        return causewayManaged(beanSort, LogicalType.infer(type));
    }

}
