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
package org.apache.causeway.viewer.wicket.ui.components.entity.header;

import org.apache.wicket.Component;

import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.core.metamodel.object.ManagedObject;
import org.apache.causeway.core.metamodel.spec.feature.ObjectAction;
import org.apache.causeway.viewer.commons.model.components.UiComponentType;
import org.apache.causeway.viewer.wicket.model.models.ActionModel;
import org.apache.causeway.viewer.wicket.model.models.UiObjectWkt;
import org.apache.causeway.viewer.wicket.ui.ComponentFactory;
import org.apache.causeway.viewer.wicket.ui.components.actionlinks.entityactions.ActionLinksPanel;
import org.apache.causeway.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.causeway.viewer.wicket.ui.util.WktComponents;

/**
 * {@link PanelAbstract Panel} representing the summary details (title, icon and
 * actions) of an entity, as per the provided {@link UiObjectWkt}.
 */
public class EntityHeaderPanel
extends PanelAbstract<ManagedObject, UiObjectWkt> {

    private static final long serialVersionUID = 1L;

    private static final String ID_ENTITY_ACTIONS = "entityActions";

    public EntityHeaderPanel(final String id, final UiObjectWkt entityModel) {
        super(id, entityModel);
    }

    public UiObjectWkt getEntityModel() {
        return getModel();
    }

    @Override
    protected void onBeforeRender() {
        buildGui();
        super.onBeforeRender();
    }

    private void buildGui() {
        addOrReplaceIconAndTitle();
        buildEntityActionsGui();
    }

    private void addOrReplaceIconAndTitle() {
        final ComponentFactory componentFactory = getComponentFactoryRegistry()
                .findComponentFactory(UiComponentType.ENTITY_ICON_TITLE_AND_COPYLINK, getEntityModel());
        final Component component = componentFactory.createComponent(getEntityModel());
        addOrReplace(component);
    }

    private void buildEntityActionsGui() {
        final UiObjectWkt model = getModel();
        var adapter = model.getObject();
        if (adapter != null) {
            var topLevelActions = ObjectAction.Util.streamTopBarActions(adapter)
            .map(act->ActionModel.forEntity(act, model))
            .collect(Can.toCan());

            ActionLinksPanel
                    .addActionLinks(this, ID_ENTITY_ACTIONS, topLevelActions,
                            ActionLinksPanel.Style.INLINE_LIST);
        } else {
            WktComponents.permanentlyHide(this, ID_ENTITY_ACTIONS);
        }
    }

}
