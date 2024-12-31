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
package org.apache.causeway.testing.fakedata.fixtures.demoapp.demomodule.dom;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

@DomainService
@Named("libFakeDataFixture.FakeDataDemoObjectWithAllMenu")
@DomainServiceLayout(
        named = "Demo"
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
public class FakeDataDemoObjectWithAllMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(sequence = "1")
    public List<FakeDataDemoObjectWithAll> listAllDemoObjectsWithAll() {
        return repositoryService.allInstances(FakeDataDemoObjectWithAll.class);
    }

    @ActionLayout(sequence = "2")
    public FakeDataDemoObjectWithAll createDemoObjectWithAll(
            final String name,
            final boolean someBoolean,
            final char someChar,
            final byte someByte,
            final short someShort,
            final int someInt,
            final long someLong,
            final float someFloat,
            final double someDouble) {
        var obj = new FakeDataDemoObjectWithAll(name);
        obj.setSomeBoolean(someBoolean);
        obj.setSomeChar(someChar);
        obj.setSomeByte(someByte);
        obj.setSomeShort(someShort);
        obj.setSomeInt(someInt);
        obj.setSomeLong(someLong);
        obj.setSomeFloat(someFloat);
        obj.setSomeDouble(someDouble);
        repositoryService.persist(obj);
        return obj;
    }

    @Inject RepositoryService repositoryService;

}
