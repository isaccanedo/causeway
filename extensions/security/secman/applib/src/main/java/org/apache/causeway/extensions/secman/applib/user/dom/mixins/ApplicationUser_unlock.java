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
package org.apache.causeway.extensions.secman.applib.user.dom.mixins;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.extensions.secman.applib.CausewayModuleExtSecmanApplib;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserStatus;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_unlock.DomainEvent;

import lombok.RequiredArgsConstructor;

/**
 *
 * @since 2.0 {@index}
 */
@Action(
        commandPublishing = Publishing.NOT_SPECIFIED,
        domainEvent = DomainEvent.class,
        executionPublishing = Publishing.NOT_SPECIFIED,
        semantics = SemanticsOf.IDEMPOTENT
)
@ActionLayout(
        associateWith = "status",
        cssClassFa = "fa-lock-open",
        sequence = "1"
)
@RequiredArgsConstructor
public class ApplicationUser_unlock {

    public static class DomainEvent
            extends CausewayModuleExtSecmanApplib.ActionDomainEvent<ApplicationUser_unlock> {}

    private final ApplicationUser target;

    @MemberSupport public ApplicationUser act() {
        target.setStatus(ApplicationUserStatus.UNLOCKED);
        return target;
    }

    @MemberSupport public String disableAct() {
        return ApplicationUserStatus.canUnlock(target.getStatus())
                ? null
                : "Status is already set to UNLOCKED";
    }

}
