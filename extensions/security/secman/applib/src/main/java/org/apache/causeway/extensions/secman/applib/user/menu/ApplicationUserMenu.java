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
package org.apache.causeway.extensions.secman.applib.user.menu;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.extensions.secman.applib.CausewayModuleExtSecmanApplib;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.causeway.extensions.secman.applib.user.man.ApplicationUserManager;

import lombok.RequiredArgsConstructor;

/**
 *
 * @since 2.0 {@index}
 */
@Named(ApplicationUserMenu.LOGICAL_TYPE_NAME)
@DomainService
@DomainServiceLayout(
        named = "Security",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ApplicationUserMenu {

    public static final String LOGICAL_TYPE_NAME = CausewayModuleExtSecmanApplib.NAMESPACE + ".ApplicationUserMenu";

    public static abstract class ActionDomainEvent<T> extends CausewayModuleExtSecmanApplib.ActionDomainEvent<T> { }

    private final ApplicationUserRepository applicationUserRepository;
    private final FactoryService factory;

    // -- ICON

    @ObjectSupport public String iconName() {
        return "applicationUser";
    }

    // -- USER MANAGER

    @Action(
            domainEvent = userManager.ActionDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    @ActionLayout(
            sequence = "100.10.1",
            cssClassFa = "user-plus"
    )
    public class userManager{

        public class ActionDomainEvent extends ApplicationUserMenu.ActionDomainEvent<userManager> { }

        @MemberSupport public ApplicationUserManager act(){
            return factory.viewModel(new ApplicationUserManager());
        }

    }

    // -- FIND USERS

    @Action(
            commandPublishing = Publishing.DISABLED,
            domainEvent = findUsers.ActionDomainEvent.class,
            executionPublishing = Publishing.DISABLED,
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(sequence = "100.10.2")
    public class findUsers{

        public class ActionDomainEvent extends ApplicationUserMenu.ActionDomainEvent<findUsers> { }

        @MemberSupport public Collection<? extends ApplicationUser> act(
                final @ParameterLayout(named = "Search") String search) {
            return applicationUserRepository.find(search);
        }

    }

}
