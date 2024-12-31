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
package org.apache.causeway.applib.services.clock;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.clock.VirtualClock;
import org.apache.causeway.applib.services.iactnlayer.InteractionContext;
import org.apache.causeway.applib.services.iactnlayer.InteractionLayerTracker;

import lombok.RequiredArgsConstructor;

/**
 * This service allows an application to be decoupled from the system time.
 * The most common use case is in support of testing scenarios, to &quot;mock the clock&quot;.
 * Use of this service also opens up the use of centralized
 * co-ordinated time management through a centralized time service.
 *
 * @since 1.x revised for 2.0 {@index}
 */
@Service
@Named(ClockService.LOGICAL_TYPE_NAME)
@javax.annotation.Priority(PriorityPrecedence.MIDPOINT)
@Qualifier("Default")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ClockService {

    static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE + ".ClockService";

    private final Provider<InteractionLayerTracker> interactionLayerTrackerProvider;

    public VirtualClock getClock() {
        return interactionLayerTrackerProvider.get().currentInteractionContext()
                .map(InteractionContext::getClock)
                .orElseGet(VirtualClock::system);
    }

    // -- SHORTCUTS

    public long getEpochMillis() {
        return getClock().nowAsEpochMilli();
    }

}
