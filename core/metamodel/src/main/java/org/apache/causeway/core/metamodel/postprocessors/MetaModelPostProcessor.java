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
package org.apache.causeway.core.metamodel.postprocessors;

import org.apache.causeway.core.metamodel.commons.MetaModelVisitor;
import org.apache.causeway.core.metamodel.context.HasMetaModelContext;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.spec.feature.ObjectAction;
import org.apache.causeway.core.metamodel.spec.feature.ObjectActionParameter;
import org.apache.causeway.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.causeway.core.metamodel.spec.feature.OneToOneAssociation;

public interface MetaModelPostProcessor
extends
    MetaModelVisitor,
    HasMetaModelContext {

    /** entry to post-processing of specified {@code objSpec} */
    default void postProcessObject(final ObjectSpecification objSpec) {}

    /** post process action - mixed-in included */
    default void postProcessAction(final ObjectSpecification objSpec, final ObjectAction act) {}

    /** post process action-parameter - mixed-in included */
    default void postProcessParameter(final ObjectSpecification objSpec, final ObjectAction act, final ObjectActionParameter param) {}

    /** post process property - mixed-in included */
    default void postProcessProperty(final ObjectSpecification objSpec, final OneToOneAssociation prop) {}

    /** post process collection - mixed-in included */
    default void postProcessCollection(final ObjectSpecification objSpec, final OneToManyAssociation coll) {}

}
