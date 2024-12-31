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
package org.apache.causeway.core.metamodel.facets.objectvalue.daterenderedadjust;

import org.apache.causeway.applib.annotation.ValueSemantics;
import org.apache.causeway.core.metamodel.facetapi.Facet;

/**
 * The amount of days to adjust a date by when rendered.
 *
 * <p>
 * Intended to be used by the viewer as a rendering hint.
 *
 * <p>
 * In the standard Apache Causeway Programming Model, corresponds to the
 * {@link ValueSemantics#dateRenderAdjustDays()} annotation.
 *
 * @since 2.0 - replacing RenderedAdjustedFacet from 1.x and 2.0.0-M6
 *
 */
public interface DateRenderAdjustFacet extends Facet {

    /**
     * The amount of days to adjust a date by when rendered.
     */
    int getDateRenderAdjustDays();

}
