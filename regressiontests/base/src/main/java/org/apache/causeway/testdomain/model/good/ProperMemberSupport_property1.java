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
package org.apache.causeway.testdomain.model.good;

import java.util.Set;

import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;

import lombok.RequiredArgsConstructor;

/**
 * For (test) mixin descriptions see {@link ProperMemberSupport}.
 */
@Property
@PropertyLayout(named = "foo", describedAs = "bar")
@RequiredArgsConstructor
public class ProperMemberSupport_property1 {

    private final ProperMemberSupport holder;

    //@Action(semantics=SAFE)   // <-- inferred (required)
    //@ActionLayout(contributed=ASSOCIATION)  // <-- inferred (required)
    public String prop() {
        return holder.toString();
    }

    // -- PROPERLY DECLARED SUPPORTING METHODS

    @MemberSupport public Set<String> autoCompleteProp(@MinLength(3) final String search) {
        return null;
    }

    @MemberSupport public Set<String> choicesProp() {
        return null;
    }

    @MemberSupport public String defaultProp() {
        return "";
    }

    @MemberSupport public String disableProp() {
        return null;
    }

    @MemberSupport public boolean hideProp() {
        return false;
    }

}
