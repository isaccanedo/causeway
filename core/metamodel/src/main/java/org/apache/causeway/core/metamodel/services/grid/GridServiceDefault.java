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
package org.apache.causeway.core.metamodel.services.grid;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.layout.grid.Grid;
import org.apache.causeway.applib.services.grid.GridLoaderService;
import org.apache.causeway.applib.services.grid.GridMarshallerService;
import org.apache.causeway.applib.services.grid.GridService;
import org.apache.causeway.applib.services.grid.GridSystemService;
import org.apache.causeway.commons.internal.base._Casts;
import org.apache.causeway.commons.internal.collections._Lists;
import org.apache.causeway.commons.internal.collections._Sets;
import org.apache.causeway.core.metamodel.CausewayModuleCoreMetamodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Default implementation of {@link GridService}.
 *
 * @since 1.x revised for 2.0 {@index}
 */
@Service
@Named(CausewayModuleCoreMetamodel.NAMESPACE + ".GridServiceDefault")
@Priority(PriorityPrecedence.MIDPOINT)
@Qualifier("Default")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GridServiceDefault implements GridService {

    public static final String COMPONENT_TNS = "https://causeway.apache.org/applib/layout/component";
    public static final String COMPONENT_SCHEMA_LOCATION = "https://causeway.apache.org/applib/layout/component/component.xsd";

    public static final String LINKS_TNS = "https://causeway.apache.org/applib/layout/links";
    public static final String LINKS_SCHEMA_LOCATION = "https://causeway.apache.org/applib/layout/links/links.xsd";

    private final GridLoaderService gridLoaderService;
    @Getter(onMethod_={@Override}) @Accessors(fluent = true)
    private final GridMarshallerService<? extends Grid> marshaller;
    private final List<GridSystemService<? extends Grid>> gridSystemServices;

    // //////////////////////////////////////

    @Override
    public boolean supportsReloading() {
        return gridLoaderService.supportsReloading();
    }

    @Override
    public void remove(final Class<?> domainClass) {
        gridLoaderService.remove(domainClass);
    }

    @Override
    public boolean existsFor(final Class<?> domainClass) {
        return gridLoaderService.existsFor(domainClass, marshaller.supportedFormats());
    }

    @Override
    public Grid load(final Class<?> domainClass) {
        return gridLoaderService.load(domainClass, marshaller).orElse(null);
    }

    @Override
    public Grid load(final Class<?> domainClass, final String layout) {
        return gridLoaderService.load(domainClass, layout, marshaller).orElse(null);
    }

    // //////////////////////////////////////

    @Override
    public Grid defaultGridFor(final Class<?> domainClass) {
        for (var gridSystemService : gridSystemServices()) {
            var grid = gridSystemService.defaultGrid(domainClass);
            if(grid != null) {
                return grid;
            }
        }
        throw new IllegalStateException(
                "No GridSystemService available to create grid for '" + domainClass.getName() + "'");
    }

    @Override
    public Grid normalize(final Grid grid) {

        if(grid.isNormalized()) {
            return grid;
        }

        var domainClass = grid.getDomainClass();

        for (var gridSystemService : gridSystemServices()) {
            gridSystemService.normalize(_Casts.uncheckedCast(grid), domainClass);
        }

        final String tnsAndSchemaLocation = tnsAndSchemaLocation(grid);
        grid.setTnsAndSchemaLocation(tnsAndSchemaLocation);

        grid.setNormalized(true);

        return grid;
    }

    @Override
    public Grid complete(final Grid grid) {

        var domainClass = grid.getDomainClass();
        for (var gridSystemService : gridSystemServices()) {
            gridSystemService.complete(_Casts.uncheckedCast(grid), domainClass);
        }

        return grid;
    }

    @Override
    public Grid minimal(final Grid grid) {

        var domainClass = grid.getDomainClass();
        for (var gridSystemService : gridSystemServices()) {
            gridSystemService.minimal(_Casts.uncheckedCast(grid), domainClass);
        }

        return grid;
    }

    /**
     * Not public API, exposed only for testing.
     */
    public String tnsAndSchemaLocation(final Grid grid) {
        var parts = _Lists.<String>newArrayList();

        parts.add(COMPONENT_TNS);
        parts.add(COMPONENT_SCHEMA_LOCATION);

        parts.add(LINKS_TNS);
        parts.add(LINKS_SCHEMA_LOCATION);

        for (var gridSystemService : getGridSystemServices()) {
            var gridImpl = gridSystemService.gridImplementation();
            if(gridImpl.isAssignableFrom(grid.getClass())) {
                parts.add(gridSystemService.tns());
                parts.add(gridSystemService.schemaLocation());
            }
        }
        return parts.stream()
                .collect(Collectors.joining(" "));
    }

    ////////////////////////////////////////////////////////

    private List<GridSystemService<? extends Grid>> filteredGridSystemServices;

    /**
     * For all of the {@link GridSystemService}s available, return only the first one for any that
     * are for the same grid implementation.
     *
     * <p>
     *   This allows default implementations (eg for bootstrap3) to be overridden while also allowing for the more
     *   general idea of multiple implementations.
     * </p>
     */
    protected List<GridSystemService<? extends Grid>> gridSystemServices() {

        if (filteredGridSystemServices == null) {

            var gridImplementations = _Sets.<Class<?>>newHashSet();

            filteredGridSystemServices = getGridSystemServices()
                    .stream()
                    // true only if gridImplementations did not already contain the specified element
                    .filter(gridService->gridImplementations.add(gridService.gridImplementation()))
                    .collect(Collectors.toList());

        }
        return filteredGridSystemServices;
    }

    // -- poor man's testing support

    List<GridSystemService<? extends Grid>> gridSystemServicesForTest;
    Collection<GridSystemService<? extends Grid>> getGridSystemServices() {
        return gridSystemServices!=null
                ? gridSystemServices
                : gridSystemServicesForTest;
    }

}
