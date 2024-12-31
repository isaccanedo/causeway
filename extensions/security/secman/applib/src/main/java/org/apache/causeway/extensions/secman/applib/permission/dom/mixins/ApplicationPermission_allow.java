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
package org.apache.causeway.extensions.secman.applib.permission.dom.mixins;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.extensions.secman.applib.CausewayModuleExtSecmanApplib;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermission;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionRule;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_allow.DomainEvent;

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
        associateWith = "rule",
        sequence = "1"
)
@RequiredArgsConstructor
public class ApplicationPermission_allow {

    public static class DomainEvent
            extends CausewayModuleExtSecmanApplib.ActionDomainEvent<ApplicationPermission_allow> {}

    private final ApplicationPermission target;

    @MemberSupport public ApplicationPermission act() {
        target.setRule(ApplicationPermissionRule.ALLOW);
        return target;
    }

    @MemberSupport public String disableAct() {
        return target.getRule() == ApplicationPermissionRule.ALLOW? "Rule is already set to ALLOW": null;
    }

}
