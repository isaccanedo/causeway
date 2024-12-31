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
package org.apache.causeway.applib.services.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.RestrictTo;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.applib.services.message.MessageService;

import lombok.RequiredArgsConstructor;

/**
 * Provides the UI to allow a current user to be impersonated.
 *
 * <p>
 *     All of the actions provided here are restricted to PROTOTYPE mode only;
 *     this feature is <i>not</i> intended for production use as it would imply
 *     a large security hole !
 * </p>
 *
 * @see UserService
 * @see ImpersonateMenuAdvisor
 *
 * @since 2.0 {@index}
 */
@DomainService
@Named(ImpersonateMenu.LOGICAL_TYPE_NAME)
@DomainServiceLayout(
        named = "Security",
        menuBar = DomainServiceLayout.MenuBar.TERTIARY
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ImpersonateMenu {

    public static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE_SUDO + ".ImpersonateMenu";   // deliberately not part of causeway.applib

    public static abstract class ActionDomainEvent<T> extends CausewayModuleApplib.ActionDomainEvent<T> {}

    final UserService userService;
    final MessageService messageService;
    final FactoryService factoryService;
    final List<ImpersonateMenuAdvisor> impersonateMenuAdvisors;

    @Action(
            commandPublishing = Publishing.DISABLED,
            domainEvent = impersonate.ActionDomainEvent.class,
            executionPublishing = Publishing.DISABLED,
            restrictTo = RestrictTo.PROTOTYPING,
            semantics = SemanticsOf.IDEMPOTENT
    )
    @ActionLayout(sequence = "100.1", cssClassFa = "fa-mask")
    public class impersonate {

        public class ActionDomainEvent extends ImpersonateMenu.ActionDomainEvent<ActionDomainEvent> { }

        /**
         * Simple implementation that is surfaced if there is no advisor.
         *
         * @param userName
         */
        @MemberSupport public void act(
                final String userName) {

            // adds generic authorized user role to indicate 'authorized' (as required by Wicket viewer)
            userService.impersonateUser(userName, Collections.singletonList(UserMemento.AUTHORIZED_USER_ROLE), null);
            messageService.informUser("Now impersonating " + userName);
        }
        @MemberSupport public boolean hideAct() {
            // when supported, either 'impersonate' or 'impersonateWithRoles' should show up but not both
            return ! userService.supportsImpersonation()
                    || ! factoryService.mixin(impersonateWithRoles.class, ImpersonateMenu.this).hideAct();
        }
        @MemberSupport public String disableAct() {
            return userService.isImpersonating() ? "currently impersonating" : null;
        }

    }

    @Action(
            commandPublishing = Publishing.DISABLED,
            domainEvent = impersonateWithRoles.ActionDomainEvent.class,
            executionPublishing = Publishing.DISABLED,
            restrictTo = RestrictTo.PROTOTYPING,
            semantics = SemanticsOf.IDEMPOTENT
    )
    @ActionLayout(sequence = "100.2", cssClassFa = "fa-mask")
    public class impersonateWithRoles {

        public class ActionDomainEvent extends ImpersonateMenu.ActionDomainEvent<impersonateWithRoles> { }

        /**
         * Impersonate a selected user, either using their current roles or
         * with a specific set of roles.
         *
         * <p>
         * This more sophisticated implementation is only available if there is
         * an {@link ImpersonateMenuAdvisor} implementation to provide the
         * choices.
         * </p>
         *
         * @param userName - user name
         * @param roleNames - role names
         * @param multiTenancyToken - multi-tenancy token
         */
        @MemberSupport public void act(
                final String userName,
                final List<String> roleNames,
                final String multiTenancyToken) {

            // adds generic authorized user role to indicate 'authorized' (as required by Wicket viewer)
            var roleNamesCopy = new ArrayList<>(roleNames);
            if(!roleNamesCopy.contains(UserMemento.AUTHORIZED_USER_ROLE)) {
                roleNamesCopy.add(UserMemento.AUTHORIZED_USER_ROLE);
            }
            userService.impersonateUser(userName, roleNamesCopy, multiTenancyToken);
            messageService.informUser("Now impersonating " + userName);
        }
        @MemberSupport public boolean hideAct() {
            return ! userService.supportsImpersonation() || choices0Act().isEmpty();
        }
        @MemberSupport public String disableAct() {
            return userService.isImpersonating() ? "currently impersonating" : null;
        }
        @MemberSupport public List<String> choices0Act() {
            return impersonateMenuAdvisor().allUserNames();
        }
        @MemberSupport public List<String> choices1Act(final String userName) {
            return impersonateMenuAdvisor().allRoleNames();
        }
        @MemberSupport public List<String> default1Act(final String userName) {
            return impersonateMenuAdvisor().roleNamesFor(userName);
        }
        @MemberSupport public String default2Act(final String userName, final List<String> roleNames) {
            return impersonateMenuAdvisor().multiTenancyTokenFor(userName);
        }
    }

    private ImpersonateMenuAdvisor impersonateMenuAdvisor() {
        // this is safe because there will always be at least one implementation.
        return impersonateMenuAdvisors.get(0);
    }

}
