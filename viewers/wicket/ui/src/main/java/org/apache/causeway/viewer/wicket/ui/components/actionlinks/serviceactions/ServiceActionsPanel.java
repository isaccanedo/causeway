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
package org.apache.causeway.viewer.wicket.ui.components.actionlinks.serviceactions;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.resource.CssResourceReference;

import org.apache.causeway.viewer.wicket.ui.pages.common.serversentevents.js.ServerSentEventsJsReference;
import org.apache.causeway.viewer.wicket.ui.util.Wkt;

// import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropdownAutoOpenJavaScriptReference;

/**
 * A panel responsible to render the application actions as menu in a navigation bar.
 *
 * <p>
 *     The multi-level sub menu support is borrowed from
 *     <a href="http://bootsnipp.com/snippets/featured/multi-level-dropdown-menu-bs">Bootsnip</a>
 * </p>
 */
class ServiceActionsPanel extends MenuActionPanel {

    private static final long serialVersionUID = 1L;

    public ServiceActionsPanel(final String id, final List<CssMenuItem> menuItems) {
        super(id);

        Wkt.listViewAdd(this, "menuItems", menuItems, listItem->{
            var menuItem = listItem.getModelObject();

            var topMenu = new WebMarkupContainer("topMenu");
            topMenu.add(subMenuItemsView(menuItem.getSubMenuItems()));
            var css = Wkt.cssNormalize(menuItem.getName());
            Wkt.cssAppend(topMenu, "top-menu-" + css);
            Wkt.labelAdd(listItem, "name", menuItem.getName());
            listItem.add(topMenu);
        });
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(
                new CssResourceReference(ServiceActionsPanel.class, "ServiceActionsPanel.css")));
        response.render(ServerSentEventsJsReference.asHeaderItem());
    }

}
