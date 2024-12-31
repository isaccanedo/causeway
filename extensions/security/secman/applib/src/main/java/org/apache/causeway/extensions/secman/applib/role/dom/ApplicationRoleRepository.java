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
package org.apache.causeway.extensions.secman.applib.role.dom;

import java.util.Collection;
import java.util.Optional;

import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;

/**
 * @since 2.0 {@index}
 */
public interface ApplicationRoleRepository {

    /**
     *
     * @return detached entity
     */
    ApplicationRole newApplicationRole();

    Collection<ApplicationRole> allRoles();

    ApplicationRole newRole(String name, String description);

    Collection<ApplicationRole> findNameContaining(String search);
    Collection<ApplicationRole> getRoles(ApplicationUser user);

    /**
     * auto-complete support
     * @param search
     */
    Collection<ApplicationRole> findMatching(String search);

    Optional<ApplicationRole> findByName(String roleName);
    Optional<ApplicationRole> findByNameCached(String roleName);

    default ApplicationRole upsert(final String name, final String roleDescription) {
        return findByName(name)
                .orElseGet(() -> newRole(name, roleDescription));
    }

    void addRoleToUser(ApplicationRole role, ApplicationUser user);
    void removeRoleFromUser(ApplicationRole role, ApplicationUser user);

    boolean isAdminRole(ApplicationRole role);

    void deleteRole(ApplicationRole holder);

}
