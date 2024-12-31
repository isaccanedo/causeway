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
package org.apache.causeway.testdomain.model.interaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.LabelPosition;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.commons.internal.base._Lazy;
import org.apache.causeway.commons.internal.base._Strings;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "InteractionDemo")
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Named("regressiontests.InteractionDemo")
@DomainObject(nature=Nature.VIEW_MODEL, editing=Editing.ENABLED)
public class InteractionDemo {

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Disabled for demonstration.")
    @XmlElement @Getter @Setter private String stringDisabled;

    @MemberSupport public List<String> autoCompleteStringDisabled(final String search) { return null;}

    @Property
    @PropertyLayout(multiLine=3, labelPosition = LabelPosition.TOP)
    @XmlElement @Getter @Setter private String stringMultiline = "initial";

    // verify, all the parameter supporting methods get picked up

    @MemberSupport public boolean hideStringMultiline() { return false; }
    @MemberSupport public String disableStringMultiline() { return null; }
    @MemberSupport public String validateStringMultiline(final String proposeValue) { return null; }
    @MemberSupport public String defaultStringMultiline() { return "default"; }
    @MemberSupport public String[] choicesStringMultiline() { return new String[] {"Hello", "World"}; }

    // -- AUTO COMPLETE

    @Property
    @XmlElement @Getter @Setter private String string2 = "initial";

    @MemberSupport public List<String> autoCompleteString2(final String search) {
        return _Strings.isEmpty(search)
                ? null
                : Stream.of(choicesStringMultiline())
                .filter(s->s.contains(search))
                .collect(Collectors.toList());
    }

    // -- COLLECTIONS

    @XmlTransient
    private final _Lazy<List<InteractionDemoItem>> lazyItems = _Lazy.threadSafe(()->
        List.of(
                InteractionDemoItem.of("first", LocalDate.of(2022, 01, 01)),
                InteractionDemoItem.of("second", LocalDate.of(2022, 01, 02)),
                InteractionDemoItem.of("third", LocalDate.of(2022, 01, 03)),
                InteractionDemoItem.of("last", LocalDate.of(2022, 01, 04)))
    );

    @Collection
    public List<InteractionDemoItem> getItems() {
        return lazyItems.get();
    }

    // -- ACTIONS WITH VARIOUS SEMANTICS

    @Action(semantics = SemanticsOf.SAFE)
    public InteractionDemo actSafely() {
        return this;
    }

    @Action(semantics = SemanticsOf.NOT_SPECIFIED)
    public InteractionDemo actUnsafely() {
        return this;
    }

    // -- ASSOCIATED ACTION WITH CHOICES FROM BULK SELECT

    @Action(choicesFrom = "items")
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public InteractionDemo doSomethingWithItems(
            // choicesFrom = "items"
            final Set<InteractionDemoItem> items0,

            // choicesFrom = "items" (repeated)
            final Set<InteractionDemoItem> items1) {

        if(items0!=null) {
            items0.forEach(item->System.out.println(item.getName()));
        }
        return this;
    }

    // -- NON-SCALAR ACTION

    @Action
    public List<InteractionDemoItem> limitedItems(final int limit) {
        return lazyItems.get().stream().limit(limit).collect(Collectors.toList());
    }

}
