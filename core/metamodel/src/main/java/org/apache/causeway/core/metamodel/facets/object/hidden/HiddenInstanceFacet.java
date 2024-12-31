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
package org.apache.causeway.core.metamodel.facets.object.hidden;

import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facets.all.hide.HiddenFacet;
import org.apache.causeway.core.metamodel.interactions.HidingInteractionAdvisor;

/**
 * Mechanism for determining whether this object is should be hidden.
 *
 * <p>
 * Even though all the properties of an object may themselves be visible, there
 * could be reasons to hide the object.
 * </p>
 *
 * <p>
 * In the standard Apache Causeway Programming Model, typically corresponds to the
 * <tt>hidden</tt> method.
 * </p>
 *
 * @see HiddenFacet
 * @see HiddenObjectFacet
 * @see HiddenTypeFacet
 *
 * @apiNote An unification attempt on HiddenTypeFacet and HiddenObjectFacet into a single,
 * failed, because both facets must co-exist, where each has veto power (not one overruling the other).
 */
public interface HiddenInstanceFacet extends Facet, HidingInteractionAdvisor {

}
