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
package org.apache.causeway.viewer.wicket.ui.components.table.head;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import org.apache.causeway.viewer.wicket.model.hints.UiHintContainer;
import org.apache.causeway.viewer.wicket.ui.components.table.DataTableWithPagesAndFilter;

class OrderByBorder<T> extends AjaxFallbackOrderByBorder<T> {

    private static final long serialVersionUID = 1L;

    private final T sortProperty;
    private final DataTableWithPagesAndFilter<?, ?> dataTable;

    private final ISortStateLocator<T> stateLocator;

    public OrderByBorder(
            final String id,
            final DataTableWithPagesAndFilter<?, ?> dataTable,
            final T sortProperty,
            final ISortStateLocator<T> stateLocator
            /* removed in wicket 8, IAjaxCallListener ajaxCallListener*/) {
        super(id, sortProperty, stateLocator/* removed in wicket 8, ajaxCallListener*/);
        this.dataTable = dataTable;
        this.stateLocator = stateLocator;
        this.sortProperty = sortProperty;
    }

    @Override
    protected void onAjaxClick(final AjaxRequestTarget target) {
        target.add(dataTable);

        final UiHintContainer uiHintContainer = getUiHintContainer();
        if(uiHintContainer == null) return;

        final ISortState<T> state = stateLocator.getSortState();
        final SortOrder order = state.getPropertySortOrder(sortProperty);

        dataTable.setSortOrderHintAndBroadcast(order, sortProperty.toString(), target);
        dataTable.setPageNumberHintAndBroadcast(target);
    }

    @Override
    protected void onSortChanged() {
        super.onSortChanged();
        // UI hint & event broadcast in onAjaxClick
        dataTable.setCurrentPage(0);
    }

    // -- HELPER

    private UiHintContainer getUiHintContainer() {
        return UiHintContainer.Util.hintContainerOf(dataTable);
    }
}
