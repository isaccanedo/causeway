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
package org.apache.causeway.applib.services.appfeatui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;

/**
 * @since 2.x  {@index}
 */
@Named(ApplicationTypeProperty.LOGICAL_TYPE_NAME)
@DomainObject
@DomainObjectLayout(
        paged = 100
)
public class ApplicationTypeProperty extends ApplicationTypeMember {

    static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE_FEAT + ".ApplicationTypeProperty";

    public static abstract class PropertyDomainEvent<T> extends ApplicationTypeMember.PropertyDomainEvent<ApplicationTypeProperty, T> {}

    // -- constructors
    public ApplicationTypeProperty() { }
    public ApplicationTypeProperty(final ApplicationFeatureId featureId) {
        super(featureId);
    }
    @Inject
    public ApplicationTypeProperty(final String memento) {
        super(memento);
    }

    // -- returnType

    @ApplicationFeatureViewModel.TypeSimpleName
    @Property(
            domainEvent = ReturnType.DomainEvent.class
    )
    @PropertyLayout(
            fieldSetId = "dataType",
            sequence = "2.6"
    )
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ReturnType {
        class DomainEvent extends PropertyDomainEvent<String> {}
    }

    @PropertyLayout(
            fieldSetId = "dataType", // TODO: shouldn't be necessary??
            sequence = "2.6"
    )
    @ReturnType
    public String getReturnType() {
        return getFeature().getActionReturnType()
                .map(Class::getSimpleName)
                .orElse("<none>");
    }

    // -- derived

    @Property(
            domainEvent = Derived.DomainEvent.class
    )
    @PropertyLayout(
            fieldSetId = "detail",
            sequence = "2.7"
    )
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Derived {
        class DomainEvent extends PropertyDomainEvent<Boolean> {}
    }

    @Derived
    public boolean isDerived() {
        return getFeature().isPropertyOrCollectionDerived();
    }

    // -- maxLength

    @Property(
            domainEvent = MaxLength.DomainEvent.class,
            optionality = Optionality.OPTIONAL
    )
    @PropertyLayout(
            fieldSetId = "detail",
            sequence = "2.8"
    )
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MaxLength {

        class DomainEvent extends PropertyDomainEvent<Integer> {}
    }

    @MaxLength
    public Integer getMaxLength() {
        var maxLen = getFeature().getPropertyMaxLength();
        return maxLen.isPresent()
                ? maxLen.getAsInt()
                : null; // unexpected code path, as this case should be hidden
    }
    @MemberSupport public boolean hideMaxLength() {
        if(!getFeature().getPropertyMaxLength().isPresent()) {
            return true;
        }
        return !String.class.getSimpleName().equals(getReturnType());
    }

    // -- typicalLength

    @Property(
            domainEvent = TypicalLength.DomainEvent.class,
            optionality = Optionality.OPTIONAL
    )
    @PropertyLayout(
            fieldSetId = "detail",
            sequence = "2.9"
    )
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TypicalLength {
        class DomainEvent extends PropertyDomainEvent<Integer> {}
    }

    @TypicalLength
    public Integer getTypicalLength() {
        var maxLen = getFeature().getPropertyTypicalLength();
        return maxLen.isPresent()
                ? maxLen.getAsInt()
                : null; // unexpected code path, as this case should be hidden
    }
    @MemberSupport public boolean hideTypicalLength() {
        if(!getFeature().getPropertyTypicalLength().isPresent()) {
            return true;
        }
        return !String.class.getSimpleName().equals(getReturnType());
    }

}
