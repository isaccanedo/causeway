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
package org.apache.causeway.core.metamodel.facets.object.defaults;

import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.apache.causeway.core.metamodel.object.ManagedObject;

/**
 * Indicates that this class has a default.
 *
 * <p>
 * The mechanism for providing a default will vary by the applib. In the Java
 * applib, this is done by implementing the DefaultProvider interface.
 *
 * <p>
 * The rest of the framework does not used this directly, but instead we infer
 * {@link PropertyDefaultFacet} from the
 * method's return type / parameter types, and copy over.
 */
public interface DefaultedFacet extends Facet {

    /**
     * The default (as a pojo, not a {@link ManagedObject}).
     */
    Object getDefault();
}
