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
package org.apache.causeway.testing.specsupport.applib.integration;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.apache.causeway.applib.services.inject.ServiceInjector;
import org.apache.causeway.commons.internal.collections._Maps;

import lombok.RequiredArgsConstructor;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.exception.CucumberException;

/**
 * @since 2.0 {@index}
 */
@RequiredArgsConstructor
public class ObjectFactoryForIntegration implements ObjectFactory {

    private final ServiceInjector serviceInjector;
    private final Map<Class<?>, Object> instances = _Maps.newHashMap();

    @Override
    public void start() { }

    @Override
    public void stop() {
        this.instances.clear();
    }

    @Override
    public boolean addClass(final Class<?> clazz) {
        return true;
    }

    @Override
    public <T> T getInstance(final Class<T> type) {
        T instance = type.cast(this.instances.get(type));
        if (instance == null) {
            instance = this.newInstance(type);

            if(serviceInjector != null) {
                instance = this.cacheInstance(type, instance);
                serviceInjector.injectServicesInto(instance);
            } else {
                // don't cache
            }
        }
        return instance;
    }

    private <T> T cacheInstance(final Class<T> type, final T instance) {
        this.instances.put(type, instance);
        return instance;
    }

    private <T> T newInstance(final Class<T> type) {
        try {
            Constructor<T> constructor = type.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException var4) {
            throw new CucumberException(String.format("%s doesn't have an empty constructor.", type), var4);
        } catch (Exception var5) {
            throw new CucumberException(String.format("Failed to instantiate %s", type), var5);
        }
    }
}
