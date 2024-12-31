/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.causeway.extensions.audittrail.applib;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.apache.causeway.extensions.audittrail.applib.app.AuditTrailMenu;
import org.apache.causeway.extensions.audittrail.applib.contributions.HasInteractionId_auditTrailEntries;
import org.apache.causeway.extensions.audittrail.applib.contributions.Object_createdByCommand;
import org.apache.causeway.extensions.audittrail.applib.contributions.Object_recentAuditTrailEntries;
import org.apache.causeway.extensions.audittrail.applib.spiimpl.EntityPropertyChangeSubscriberForAuditTrail;

@Configuration
@Import({
        AuditTrailMenu.class,
        EntityPropertyChangeSubscriberForAuditTrail.class,

        HasInteractionId_auditTrailEntries.class,
        Object_recentAuditTrailEntries.class,
        Object_createdByCommand.class,
})
public class CausewayModuleExtAuditTrailApplib {

    public static final String NAMESPACE = "causeway.ext.auditTrail";
    public static final String SCHEMA = "causewayExtAuditTrail";

    public abstract static class TitleUiEvent<S>
            extends org.apache.causeway.applib.events.ui.TitleUiEvent<S> { }

    public abstract static class IconUiEvent<S>
            extends org.apache.causeway.applib.events.ui.IconUiEvent<S> { }

    public abstract static class CssClassUiEvent<S>
            extends org.apache.causeway.applib.events.ui.CssClassUiEvent<S> { }

    public abstract static class LayoutUiEvent<S>
            extends org.apache.causeway.applib.events.ui.LayoutUiEvent<S> { }

    public abstract static class ActionDomainEvent<S>
    extends org.apache.causeway.applib.events.domain.ActionDomainEvent<S> {}

    public abstract static class CollectionDomainEvent<S, T>
    extends org.apache.causeway.applib.events.domain.CollectionDomainEvent<S, T> {}

    public abstract static class PropertyDomainEvent<S, T>
    extends org.apache.causeway.applib.events.domain.PropertyDomainEvent<S, T> {}

}
