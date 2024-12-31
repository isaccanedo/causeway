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

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;
import org.apache.causeway.applib.services.appfeat.ApplicationFeatureRepository;
import org.apache.causeway.applib.services.appfeatui.ApplicationFeatureViewModel;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.extensions.secman.applib.CausewayModuleExtSecmanApplib;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermission;

import lombok.RequiredArgsConstructor;

/**
 *
 * @since 2.0 {@index}
 */
@Property(
        domainEvent = ApplicationPermission_feature.PropertyDomainEvent.class
)
@PropertyLayout(
        fieldSetId="feature", sequence = "4",
        hidden=Where.REFERENCES_PARENT
)
@RequiredArgsConstructor
public class ApplicationPermission_feature {

    public static class PropertyDomainEvent
            extends CausewayModuleExtSecmanApplib.PropertyDomainEvent<ApplicationPermission_feature, ApplicationFeatureViewModel> {}

    final ApplicationPermission target;

    @Inject FactoryService factory;
    @Inject ApplicationFeatureRepository featureRepository;

    @MemberSupport public ApplicationFeatureViewModel prop(final ApplicationPermission permission) {
        if(permission.getFeatureSort() == null) {
            return null;
        }
        final ApplicationFeatureId featureId = getFeatureId(permission);
        return ApplicationFeatureViewModel.newViewModel(featureId, featureRepository, factory);
    }

    private static ApplicationFeatureId getFeatureId(final ApplicationPermission permission) {
        return ApplicationFeatureId.newFeature(permission.getFeatureSort(), permission.getFeatureFqn());
    }

}
