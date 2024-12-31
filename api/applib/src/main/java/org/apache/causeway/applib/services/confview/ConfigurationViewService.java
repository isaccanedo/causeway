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
package org.apache.causeway.applib.services.confview;

import java.util.Set;

/**
 * Returns the configuration properties (as view models) such that they can
 * be rendered into the UI (in the Wicket Viewer, under the "Configuration"
 * menu).
 *
 * <p>
 *     This is the backing service used by {@link ConfigurationMenu}.
 * </p>
 *
 * @since 2.0 {@index}
 */
public interface ConfigurationViewService {

    enum Scope {
        ENV,
        PRIMARY,
        SECONDARY,
    }

    /**
     * Returns all properties, each as an instance of {@link ConfigurationProperty} (a view model).
     * Mask sensitive values if required.
     */
    Set<ConfigurationProperty> getConfigurationProperties(Scope scope);

}
