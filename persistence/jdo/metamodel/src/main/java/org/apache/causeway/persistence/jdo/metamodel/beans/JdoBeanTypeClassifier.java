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
package org.apache.causeway.persistence.jdo.metamodel.beans;

import java.util.Locale;

import javax.jdo.annotations.EmbeddedOnly;

import org.apache.causeway.applib.id.LogicalType;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.commons.internal.reflection._Annotations;
import org.apache.causeway.core.config.beans.CausewayBeanMetaData;
import org.apache.causeway.core.config.beans.CausewayBeanTypeClassifier;
import org.apache.causeway.core.config.beans.PersistenceStack;

/**
 * ServiceLoader plugin, classifies PersistenceCapable types into BeanSort.ENTITY.
 * @since 2.0
 */
public class JdoBeanTypeClassifier implements CausewayBeanTypeClassifier {

    @Override
    public CausewayBeanMetaData classify(final Class<?> type) {

        var persistenceCapableAnnot = _Annotations
                .synthesize(type, javax.jdo.annotations.PersistenceCapable.class);
        if(persistenceCapableAnnot.isPresent()) {

            var embeddedOnlyAttribute = persistenceCapableAnnot.get().embeddedOnly();
            // Whether objects of this type can only be embedded,
            // hence have no ID that binds them to the persistence layer
            final boolean embeddedOnly = Boolean.valueOf(embeddedOnlyAttribute)
                    || _Annotations.synthesize(type, EmbeddedOnly.class).isPresent();
            if(embeddedOnly) {
                return null; // don't categorize as entity ... fall through in the caller's logic
            }

            var logicalType = LogicalType.infer(type);

            // don't trample over the @Named(=...) if present
            if(logicalType.getLogicalTypeName().equals(type.getName())) {
                var schema = persistenceCapableAnnot.get().schema();
                if(_Strings.isNotEmpty(schema)) {

                    var table = persistenceCapableAnnot.get().table();

                    var logicalTypeName = String.format("%s.%s",
                            schema.toLowerCase(Locale.ROOT),
                            _Strings.isNotEmpty(table)
                                ? table
                                : type.getSimpleName());

                    logicalType = LogicalType.eager(type, logicalTypeName);

                }
            }

            return CausewayBeanMetaData.entity(PersistenceStack.JDO, logicalType);
        }

        return null; // we don't see fit to classify given type
    }

}
