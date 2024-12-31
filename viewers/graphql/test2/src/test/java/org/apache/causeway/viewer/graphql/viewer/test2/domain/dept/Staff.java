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
package org.apache.causeway.viewer.graphql.viewer.test2.domain.dept;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.SemanticsOf;

import lombok.RequiredArgsConstructor;

@Named("university.dept.Staff")
@DomainService
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Staff {

    final StaffMemberRepository staffMemberRepository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public StaffMember createStaffMember(
            final String name,
            final Department department
    ){
        return staffMemberRepository.create(name, department);
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<StaffMember> findAllStaffMembers(){
        return staffMemberRepository.findAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    public StaffMember findStaffMemberByName(final String name){
        return staffMemberRepository.findByName(name);
    }

}
