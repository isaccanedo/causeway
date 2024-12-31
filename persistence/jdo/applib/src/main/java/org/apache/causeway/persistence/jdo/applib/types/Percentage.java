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
package org.apache.causeway.persistence.jdo.applib.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;

/**
 * Meta-annotation for an optional {@link java.math.BigDecimal} property or parameter
 * representing a percentage amount.
 *
 * @since 2.0 {@index}
 */
@Property(
        optionality = Optionality.OPTIONAL
)
@PropertyLayout(
)
@Parameter(
        optionality = Optionality.OPTIONAL
)
@ParameterLayout(
)
@javax.validation.constraints.Digits(
        integer = Percentage.INTEGER,
        fraction = Percentage.FRACTION
)
@javax.jdo.annotations.Column(allowsNull = "true", length = Percentage.INTEGER + Percentage.FRACTION, scale = Percentage.FRACTION)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Percentage {

    @AliasFor( annotation = Property.class, attribute = "optionality")
    Optionality propertyOptionality() default Optionality.OPTIONAL;
    @AliasFor( annotation = Parameter.class, attribute = "optionality")
    Optionality parameterOptionality() default Optionality.OPTIONAL;

    @AliasFor( annotation = javax.jdo.annotations.Column.class, attribute = "allowsNull")
    String columnAllowsNull() default "true";
    @AliasFor( annotation = javax.jdo.annotations.Column.class, attribute = "length")
    int columnLength() default Percentage.INTEGER + Percentage.FRACTION;
    @AliasFor( annotation = javax.jdo.annotations.Column.class, attribute = "scale")
    int columnScale() default Percentage.FRACTION;

    int INTEGER = 3;
    @AliasFor( annotation = javax.validation.constraints.Digits.class, attribute = "integer")
    int digitsInteger() default Percentage.INTEGER;

    int FRACTION = 2;
    @AliasFor( annotation = javax.validation.constraints.Digits.class, attribute = "fraction")
    int digitsFraction() default Percentage.FRACTION;

}
