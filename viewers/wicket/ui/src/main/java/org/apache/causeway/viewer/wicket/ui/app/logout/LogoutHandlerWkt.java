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
package org.apache.causeway.viewer.wicket.ui.app.logout;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.request.cycle.RequestCycle;

import org.springframework.stereotype.Service;

import org.apache.causeway.applib.services.iactnlayer.InteractionLayerTracker;
import org.apache.causeway.core.interaction.session.CausewayInteraction;
import org.apache.causeway.core.security.authentication.logout.LogoutHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutHandlerWkt implements LogoutHandler {

    final InteractionLayerTracker interactionLayerTracker;

    @Override
    public void logout() {

        if(RequestCycle.get()==null) {
            return; // logout is only permissive if within the context of a Wicket request-cycle
        }

        var currentWktSession = AuthenticatedWebSession.get();
        if(currentWktSession==null) {
            return;
        }

        if(interactionLayerTracker.isInInteraction()) {
            interactionLayerTracker.currentInteraction()
            .map(CausewayInteraction.class::cast)
            .ifPresent(interaction->
                interaction.setOnClose(currentWktSession::invalidateNow));

        } else {
            currentWktSession.invalidateNow();
        }
    }

}
