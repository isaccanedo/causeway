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
package org.apache.causeway.applib.services.sitemap;

import javax.inject.Named;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.RestrictTo;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.value.Clob;
import org.apache.causeway.commons.internal.base._Strings;

/**
 * Simply provides a UI to allow a site-map (obtained from {@link SitemapService}) to be downloaded.
 *
 * @since 2.x {@index}
 */
@Named(SitemapServiceMenu.LOGICAL_TYPE_NAME)
@DomainService
@DomainServiceLayout(
        named = "Prototyping",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
public class SitemapServiceMenu {

    public static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE + ".SitemapServiceMenu";

    public static abstract class ActionDomainEvent<T> extends CausewayModuleApplib.ActionDomainEvent<T> {}

    private final SitemapService sitemapService;

    public SitemapServiceMenu(final SitemapService sitemapService) {
        this.sitemapService = sitemapService;
    }

    @Action(
            commandPublishing = Publishing.DISABLED,
            domainEvent = downloadSitemap.ActionDomainEvent.class,
            executionPublishing = Publishing.DISABLED,
            restrictTo = RestrictTo.PROTOTYPING,
            semantics = SemanticsOf.NON_IDEMPOTENT //disable client-side caching
            )
    @ActionLayout(
            cssClassFa = "fa-download",
            named = "Download Site-Map Template (Adoc)",
            sequence="500.450.1"
    )
    public class downloadSitemap{

        public class ActionDomainEvent extends SitemapServiceMenu.ActionDomainEvent<downloadSitemap> {}

        @MemberSupport public Clob act(
                @Parameter(optionality = Optionality.MANDATORY) final String title,
                @Parameter(optionality = Optionality.MANDATORY) final String fileName) {

            final String adoc = sitemapService.toSitemapAdoc(title);

            return new Clob(_Strings.asFileNameWithExtension(fileName,  ".adoc"), "text/plain", adoc);
        }

        @MemberSupport public String default0Act() { return "Site-Map"; }
        @MemberSupport public String default1Act() { return "sitemap.adoc"; }

    }

}
