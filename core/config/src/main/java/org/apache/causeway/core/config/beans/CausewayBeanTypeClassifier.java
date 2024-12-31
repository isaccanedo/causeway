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

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.context._Context;
import org.apache.causeway.commons.internal.reflection._ClassCache;

import lombok.NonNull;

/**
 * ServiceLoader SPI that allows for implementing instances to have a say during bean type scanning.
 * @since 2.0
 */
public interface CausewayBeanTypeClassifier {

    public enum Attributes {
        /**
         * Corresponds to presence of a {@link DomainService} annotation.
         * @see _ClassCache#lookupAttribute(Class, String)
         */
        HAS_DOMAIN_SERVICE_SEMANTICS,

        /**
         * Corresponds to {@link DomainObject#mixinMethod()}.
         * @see _ClassCache#lookupAttribute(Class, String)
         */
        MIXIN_MAIN_METHOD_NAME;

        public void set(final _ClassCache classCache, final Class<?> type, final String attributeValue) {
            classCache.setAttribute(type, this.name(), attributeValue);
        }
        public Optional<String> lookup(final _ClassCache classCache, final Class<?> type) {
            return classCache.lookupAttribute(type, this.name());
        }
    }

    // -- INTERFACE

    /**
     * Returns the bean classification for given {@code type}.
     *
     * @apiNote Initially used to collect all concrete types that are considered by Spring
     * for type inspection, most likely without any {@code context} yet being available,
     * but later used by the {@code SpecificationLoader} to also
     * classify non-concrete types (interfaces and abstract classes).
     */
    CausewayBeanMetaData classify(Class<?> type);

    // -- FACTORY

    /**
     * in support of JUnit testing
     */
    static CausewayBeanTypeClassifier createInstance() {
        return new CausewayBeanTypeClassifierDefault(Can.empty());
    }

    static CausewayBeanTypeClassifier createInstance(final @NonNull ApplicationContext applicationContext) {
        return new CausewayBeanTypeClassifierDefault(
                Can.ofArray(applicationContext.getEnvironment().getActiveProfiles()));
    }

    // -- LOOKUP

    public static Can<CausewayBeanTypeClassifier> get() {
        return Can.ofCollection(SpringFactoriesLoader
                .loadFactories(CausewayBeanTypeClassifier.class, _Context.getDefaultClassLoader()));
    }

}
