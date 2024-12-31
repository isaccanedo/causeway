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
package org.apache.causeway.extensions.secman.applib.tenancy.dom;

import java.util.Collection;

import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;

/**
 * @since 2.0 {@index}
 */
public interface ApplicationTenancyRepository {

    /**
     *
     * @return detached entity
     */
    ApplicationTenancy newApplicationTenancy();

    Collection<ApplicationTenancy> allTenancies();
    /** non-parented, not a child */
    Collection<ApplicationTenancy> getRootTenancies();
    Collection<ApplicationTenancy> getChildren(ApplicationTenancy tenancy);
    Collection<ApplicationTenancy> findByNameOrPathMatchingCached(String partialNameOrPath);
    ApplicationTenancy findByPath(String path);

    /**
     * auto-complete support
     * @param search
     */
    Collection<ApplicationTenancy> findMatching(String search);

    ApplicationTenancy newTenancy(String name, String path, ApplicationTenancy parent);

    void setTenancyOnUser(ApplicationTenancy tenancy, ApplicationUser user);
    void clearTenancyOnUser(ApplicationUser user);

    void setParentOnTenancy(ApplicationTenancy tenancy, ApplicationTenancy parent);
    void clearParentOnTenancy(ApplicationTenancy tenancy);

}
