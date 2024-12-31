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
package org.apache.causeway.applib.events.domain;

import org.apache.causeway.applib.util.ObjectContracts;
import org.apache.causeway.applib.util.ToString;

import lombok.Getter;
import lombok.Setter;

/**
 * Fired whenever the framework interacts with a domain object's property.
 *
 * <p>
 * This is the specialization of {@link AbstractDomainEvent}, for properties,
 *  * which should then be further subclassed by domain application.
 * </p>
 *
 * <p>
 * The class has a number of responsibilities (in addition to those it
 * inherits):
 * </p>
 *
 * <ul>
 *     <li>
 *          capture the old and new values of the property
 *     </li>
 * </ul>
 *
 * <p>
 * The class itself is instantiated automatically by the framework using a
 * no-arg constructor; fields are set reflectively.
 * </p>
 *
 * @since 1.x {@index}
 */
public abstract class PropertyDomainEvent<S,T> extends AbstractDomainEvent<S> {

    /**
     * This class is the default for the
     * {@link org.apache.causeway.applib.annotation.Property#domainEvent()}
     * annotation attribute.
     *
     * <p>
     * Whether this raises an event or not depends upon the
     * <tt>causeway.applib.annotation.property.domain-event.post-for-default</tt>
     * configuration property.
     * </p>
     */
    public static class Default extends PropertyDomainEvent<Object, Object> {}

    /**
     * Convenience class to use indicating that an event should <i>not</i> be
     * posted (irrespective of the configuration property setting for the
     * {@link Default} event.
     */
    public static class Noop extends PropertyDomainEvent<Object, Object> {}

    /**
     * Convenience class meaning that an event <i>should</i> be posted
     * (irrespective of the configuration property setting for the
     * {@link Default} event..
     */
    public static class Doop extends PropertyDomainEvent<Object, Object> {}

    /**
     * Subtypes can define a no-arg constructor; the framework sets state
     * via (non-API) setters.
     */
    public PropertyDomainEvent() {
    }

    /**
     * Subtypes can define a one-arg constructor; the framework sets state
     * via (non-API) setters.
     * <p>
     * A one-arg constructor is particularly useful in the context of non-static
     * DomainEvent class nesting.
     * @apiNote The Java compiler implicitly adds a constructor
     * to the non-static nested class, receiving the parent type as single argument.
     */
    public PropertyDomainEvent(final S source) {
        super(source);
    }

    /**
     * The current (pre-modification) value of the property.
     *
     * <p>
     * Populated at {@link org.apache.causeway.applib.events.domain.AbstractDomainEvent.Phase#VALIDATE} and subsequent
     * phases (but null for
     * {@link org.apache.causeway.applib.events.domain.AbstractDomainEvent.Phase#HIDE hidden}
     * and {@link org.apache.causeway.applib.events.domain.AbstractDomainEvent.Phase#DISABLE disable}
     * phases).
     * </p>
     */
    @Getter @Setter
    private T oldValue;

    /**
     * The proposed (post-modification) value of the property.
     *
     * <p>
     * Populated at
     * {@link org.apache.causeway.applib.events.domain.AbstractDomainEvent.Phase#VALIDATE}
     * and subsequent phases (but null for
     * {@link Phase#HIDE hidden}
     * and {@link org.apache.causeway.applib.events.domain.AbstractDomainEvent.Phase#DISABLE disable}
     * phases).
     * </p>
     *
     * <p>
     *     The proposed new value can also be modified by event handlers
     *     during the
     *     {@link org.apache.causeway.applib.events.domain.AbstractDomainEvent.Phase#EXECUTING executing}
     *     phase.  The new value must be the same type as the expected value;
     *     the framework performs no sanity checks.
     * </p>
     */
    @Getter @Setter
    private T newValue;

    private static final ToString<PropertyDomainEvent<?,?>> toString =
            ObjectContracts.<PropertyDomainEvent<?,?>>
    toString("source", PropertyDomainEvent::getSource)
    .thenToString("identifier", PropertyDomainEvent::getIdentifier)
    .thenToString("eventPhase", PropertyDomainEvent::getEventPhase)
    .thenToString("oldValue", PropertyDomainEvent::getOldValue)
    .thenToString("newValue", PropertyDomainEvent::getNewValue)
    ;

    @Override
    public String toString() {
        return toString.toString(this);
    }

}
