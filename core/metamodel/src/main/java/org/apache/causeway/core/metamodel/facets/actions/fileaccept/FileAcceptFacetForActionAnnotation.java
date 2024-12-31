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
package org.apache.causeway.core.metamodel.facets.actions.fileaccept;

import java.util.Optional;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.objectvalue.fileaccept.FileAcceptFacet;
import org.apache.causeway.core.metamodel.facets.objectvalue.fileaccept.FileAcceptFacetAbstract;

public class FileAcceptFacetForActionAnnotation
extends FileAcceptFacetAbstract {

    public static Optional<FileAcceptFacet> create(
            final Optional<Action> actionIfAny,
            final FacetHolder holder) {

        return actionIfAny
                .map(Action::fileAccept)
                .filter(_Strings::isNotEmpty)
                .map(fileAccept -> new FileAcceptFacetForActionAnnotation(fileAccept, holder));
    }

    private FileAcceptFacetForActionAnnotation(final String value, final FacetHolder holder) {
        super(value, holder);
    }

}
