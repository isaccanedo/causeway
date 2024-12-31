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
package org.apache.causeway.applib.layout.grid.bootstrap;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Superclass for all layout classes, factoring out the common {@link #getCssClass()} attribute.
 *
 * @since 1.x {@index}
 */
public abstract class BSElementAbstract implements BSElement {

    private static final long serialVersionUID = 1L;

    private String cssClass;

    /**
     * Any additional CSS classes to render on the page element corresponding to this object,
     * eg as per the <a href="http://getbootstrap.com/css/#grid-less">Bootstrap mixins</a> or just for
     * custom styling.
     */
    @Override
    @XmlAttribute(required = false)
    public String getCssClass() {
        return cssClass;
    }

    @Override
    public void setCssClass(final String cssClass) {
        this.cssClass = cssClass;
    }

}
