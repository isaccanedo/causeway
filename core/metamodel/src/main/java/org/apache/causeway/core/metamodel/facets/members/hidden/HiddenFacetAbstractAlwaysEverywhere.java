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
package org.apache.causeway.core.metamodel.facets.members.hidden;

import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.object.ManagedObject;

public abstract class HiddenFacetAbstractAlwaysEverywhere extends HiddenFacetAbstract {

    public HiddenFacetAbstractAlwaysEverywhere(
            final FacetHolder holder) {
        super(Where.ANYWHERE, holder);
    }

    /**
     * Always returns <i>Always hidden</i>.
     */
    @Override
    public String hiddenReason(final ManagedObject target, Where where) {
        return "Always hidden";
    }

}
