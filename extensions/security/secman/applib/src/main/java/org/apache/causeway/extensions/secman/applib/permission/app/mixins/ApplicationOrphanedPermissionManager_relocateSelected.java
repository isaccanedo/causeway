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
package org.apache.causeway.extensions.secman.applib.permission.app.mixins;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.appfeat.ApplicationFeature;
import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;
import org.apache.causeway.applib.services.appfeat.ApplicationFeatureRepository;
import org.apache.causeway.extensions.secman.applib.CausewayModuleExtSecmanApplib;
import org.apache.causeway.extensions.secman.applib.permission.app.ApplicationOrphanedPermissionManager;
import org.apache.causeway.extensions.secman.applib.permission.app.mixins.ApplicationOrphanedPermissionManager_relocateSelected.DomainEvent;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermission;

import lombok.RequiredArgsConstructor;

/**
 *
 * @since 2.0 {@index}
 */
@Action(
        choicesFrom = "orphanedPermissions",
        commandPublishing = Publishing.NOT_SPECIFIED,
        domainEvent = DomainEvent.class,
        executionPublishing = Publishing.NOT_SPECIFIED,
        semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE
)
@ActionLayout(
        associateWith = "orphanedPermissions",
        describedAs = "For the selected permissions, relocates to the specified namespace"
)
@RequiredArgsConstructor
public class ApplicationOrphanedPermissionManager_relocateSelected {

    public static class DomainEvent
            extends CausewayModuleExtSecmanApplib.ActionDomainEvent<ApplicationOrphanedPermissionManager_relocateSelected> {}

    @Inject private ApplicationFeatureRepository featureRepository;

    private final ApplicationOrphanedPermissionManager target;

    @MemberSupport public ApplicationOrphanedPermissionManager act(
            final Collection<ApplicationPermission> permissions,
            @Parameter(optionality = Optionality.MANDATORY)
            final String targetNamespace) {

        permissions.forEach(perm->relocate(perm, targetNamespace));
        return target;
    }

    @MemberSupport public Collection<String> choices1Act() {
        return featureRepository.allNamespaces().stream()
                    .map(ApplicationFeature::getFullyQualifiedName)
                    .collect(Collectors.toCollection(TreeSet::new));
    }

    private void relocate(
            final ApplicationPermission permission,
            final String targetNamespace) {

        var appFeatureId = ApplicationFeatureId.newFeature(
                permission.getFeatureSort(),
                permission.getFeatureFqn());

        var relocatedFqn = appFeatureId
                .withNamespace(targetNamespace)
                .getFullyQualifiedName();

        permission.setFeatureFqn(relocatedFqn);
    }

}
