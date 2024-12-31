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
package org.apache.causeway.extensions.executionlog.applib.contributions;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.mixins.security.HasUsername;
import org.apache.causeway.extensions.executionlog.applib.CausewayModuleExtExecutionLogApplib;
import org.apache.causeway.extensions.executionlog.applib.dom.ExecutionLogEntry;
import org.apache.causeway.extensions.executionlog.applib.dom.ExecutionLogEntryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Contributes the <code>recentExecutionsByUser</code> collection to any domain object implementing {@link HasUsername}.
 *
 * <p>
 *     For example the <i>secman</i> extension's <code>ApplicationUser</code> entity implements this interface.
 * </p>
 * @since 2.0 {@index}
 */
@Collection(
    domainEvent = HasUsername_recentExecutionsByUser.CollectionDomainEvent.class
)
@CollectionLayout(
    defaultView = "table",
    paged = 5,
    sequence = "3"
)
@RequiredArgsConstructor
public class HasUsername_recentExecutionsByUser {

    public static class CollectionDomainEvent
            extends CausewayModuleExtExecutionLogApplib.CollectionDomainEvent<HasUsername_recentExecutionsByUser, ExecutionLogEntry> { }

    private final HasUsername hasUsername;

    @MemberSupport public List<? extends ExecutionLogEntry> coll() {
        var username = hasUsername.getUsername();
        return username != null
                ? executionLogEntryRepository.findRecentByUsername(username)
                : Collections.emptyList();
    }
    @MemberSupport public boolean hideColl() {
        return hasUsername.getUsername() == null;
    }

    @Inject ExecutionLogEntryRepository executionLogEntryRepository;
}
