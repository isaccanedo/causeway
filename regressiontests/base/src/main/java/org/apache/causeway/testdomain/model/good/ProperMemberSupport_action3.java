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

import java.util.List;
import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.Publishing;

import lombok.RequiredArgsConstructor;

/**
 * For (test) mixin descriptions see {@link ProperMemberSupport}.
 */
@Action(executionPublishing = Publishing.ENABLED)
@RequiredArgsConstructor
public class ProperMemberSupport_action3 {

    private final ProperMemberSupport holder;

    // proper mix-in action

    @MemberSupport public ProperMemberSupport act(final List<String> p0, final List<String> p1) {
        return holder;
    }

    @MemberSupport public String disableAct() {
        return null;
    }

    @MemberSupport public boolean hideAct() {
        return false;
    }

    @MemberSupport public String validateAct(final List<String> p0, final List<String> p1) {
        return null;
    }

    @MemberSupport public Set<String> autoComplete0Act(@MinLength(3) final String search) {
        return null;
    }

    @MemberSupport public Set<String> autoComplete1Act(@MinLength(3) final String search) {
        return null;
    }

// variant with dependent arg
//    @MemberSupport
//    public Set<String> autoComplete1$$(String p0, @MinLength(3) String search) {
//        return null;
//    }

    @MemberSupport public Set<String> choices0Act() {
        return null;
    }

    @MemberSupport public Set<String> choices1Act() {
        return null;
    }

// variant with dependent arg
//    @MemberSupport
//    public Set<String> choices1$$(String p0) {
//        return null;
//    }

    @MemberSupport public List<String> default0Act() {
        return null;
    }

    @MemberSupport public List<String> default1Act() {
        return null;
    }

    @MemberSupport public String validate0Act(final List<String> p0) {
        return null;
    }

    @MemberSupport public String validate1Act(final List<String> p1) {
        return null;
    }

}
