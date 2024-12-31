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

import javax.xml.bind.annotation.XmlType;

/**
 * How prompting for new values of a property/arguments for an action should be performed
 * @since 1.x {@index}
 */
@XmlType(
        namespace = "https://causeway.apache.org/applib/layout/component"
        )
public enum PromptStyle {
    /**
     * Prompt using the style configured by <tt>causeway.viewer.wicket.promptStyle</tt>.
     *
     * <p>
     *      If no style is configured, then {@link #INLINE} is assumed.
     * </p>
     */
    AS_CONFIGURED,
    /**
     * Use a dialog for the prompt.
     *
     * <p>
     *     This will be either modal dialog (same as if {@link #DIALOG_MODAL} was selected) or sidebar (same as if
     *     {@link #DIALOG_SIDEBAR} was selected) depending on the value of the
     *     <code>causeway.viewer.wicket.dialogMode</code> configuration property.
     * </p>
     */
    DIALOG,
    /**
     * Use a dialog for the prompt, rendered in a sidebar.
     */
    DIALOG_SIDEBAR,
    /**
     * Use a dialog for the prompt, rendered in a modal dialog.
     */
    DIALOG_MODAL,
    /**
     * Show the form inline, temporarily replacing the rendering of the property.
     */
    INLINE,
    /**
     * Applies only to actions, show the form inline, invoked as if editing the property.
     *
     * <p>Note that:
     * <ul>
     * <li>
     *     Only one such action should have this attribute set per property.  If there are multiple actions, then
     *     the first one discovered with the attribute is used.
     * </li>
     * <li>
     *     If the property is editable, then this attribute is ignored (and the action is treated as having a prompt style of {@link #INLINE}).
     * </li>
     * <li>
     *     If applied to a property, then the property's prompt style is simply treated as {@link #INLINE}.
     * </li>
     * </ul>
     * </p>
     *
     */
    INLINE_AS_IF_EDIT,
    /**
     * Ignore the value provided by this annotation (meaning that the framework will keep searching, in meta
     * annotations or superclasses/interfaces).
     */
    NOT_SPECIFIED;
    public boolean isDialogAny() { return this == DIALOG || this == DIALOG_MODAL || this == DIALOG_SIDEBAR; }
    public boolean isInline() { return this == INLINE; }
    public boolean isInlineAsIfEdit() { return this == INLINE_AS_IF_EDIT; }
    public boolean isInlineAny() { return this == INLINE || this == INLINE_AS_IF_EDIT; }

}
