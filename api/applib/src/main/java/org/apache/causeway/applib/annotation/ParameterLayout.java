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
package org.apache.causeway.applib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Collects together all layout hints for action parameters in a single
 * annotation.
 *
 * @since 1.x {@index}
 *
 * @see ActionLayout
 * @see PropertyLayout
 * @see Parameter
 */
@Inherited
@Target({
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterLayout {

    /**
     * Indicates the css class that a parameter should have, to
     * allow more targeted styling in <code>application.css</code>.
     *
     * @see PropertyLayout#cssClass()
     * @see ActionLayout#cssClass()
     * @see CollectionLayout#cssClass()
     * @see DomainObjectLayout#cssClass()
     */
    String cssClass()
            default "";

    /**
     * Description of this property, eg to be rendered in a tooltip.
     *
     * @see PropertyLayout#describedAs()
     * @see ActionLayout#describedAs()
     * @see CollectionLayout#describedAs()
     * @see DomainObjectLayout#describedAs()
     */
    String describedAs()
            default "";

    /**
     * In forms, positioning of the label (left, top or none) relative to the parameter value.
     *
     * <p>
     * If not specified, the default depends upon the parameter value's
     * datatype (including whether the field is {@link #multiLine()}.
     * </p>
     *
     * @see #multiLine()
     */
    LabelPosition labelPosition()
            default LabelPosition.NOT_SPECIFIED;

    /**
     * Name of this action parameter.
     *
     * <p>
     *     The name of an action parameters is available to the framework
     *     provided that the code is compiled using <code>-parameters</code>
     *     (for <code>javac</code>.
     * </p>
     *
     * <p>
     * The only reason then to name the parameter is if the desired name is a reserved Java keyword, such as `default` or `package`.
     * </p>
     *
     * @see ActionLayout#named()
     * @see PropertyLayout#named()
     * @see CollectionLayout#named()
     * @see DomainObjectLayout#named()
     * @see DomainServiceLayout#named()
     */
    String named()
            default "";

    /**
     * For string parameters (and properties), whether to render as a text area
     * over multiple lines.
     *
     * <p>
     *     If set to &gt; 1, then {@link #labelPosition()} defaults to
     *     {@link LabelPosition#TOP top}.
     * </p>
     *
     * @see #labelPosition()
     * @see PropertyLayout#multiLine()
     */
    int multiLine()
            default -1;

    /**
     * The typical entry length of a field, use to determine the optimum width for display
     *
     * <p>
     *     Note: the Wicket viewer does not use this information.
     * </p>
     *
     * @see PropertyLayout#typicalLength()
     */
    int typicalLength()
            default -1;

}
