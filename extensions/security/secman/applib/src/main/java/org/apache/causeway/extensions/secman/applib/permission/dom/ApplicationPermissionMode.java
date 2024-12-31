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
package org.apache.causeway.extensions.secman.applib.permission.dom;

import org.apache.causeway.applib.services.appfeat.ApplicationFeature;
import org.apache.causeway.commons.internal.base._Strings;

/**
 * Named after UNIX modes (<code>chmod</code> etc), determines that nature of access (of denial of access if vetoed)
 * to an {@link ApplicationFeature}.
 *
 * @since 2.0 {@index}
 */
public enum ApplicationPermissionMode {
    /**
     * Whether the user/role can view (or is prevented from viewing) the application feature (class member).
     * <p>
     * The {@link ApplicationPermissionRule rule} of the
     * {@link ApplicationPermission} indicates whether access is being
     * granted or denied.
     */
    VIEWING,
    /**
     * Whether can user/role can change (or is prevented from changing) the state of the system using the application feature (class member).
     * <p>
     * In other words, whether they can execute (if an action, modify/clear (if a property), [addTo/removeFrom
     * (if a collection) ... see note below].
     * <p>
     * The {@link ApplicationPermissionRule rule} of the
     * {@link ApplicationPermission} indicates whether access is being
     * granted or denied.
     * <p>
     * Note: as of CAUSEWAY-3084 the notion of mutable collections was removed
     */
    CHANGING;

    @Override
    public String toString() {
        return _Strings.capitalize(name());
    }

}
