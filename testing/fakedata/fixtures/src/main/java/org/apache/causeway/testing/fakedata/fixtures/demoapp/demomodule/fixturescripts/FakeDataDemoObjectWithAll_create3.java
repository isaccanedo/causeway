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
package org.apache.causeway.testing.fakedata.fixtures.demoapp.demomodule.fixturescripts;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.commons.internal.collections._Lists;
import org.apache.causeway.testing.fakedata.applib.services.FakeDataService;
import org.apache.causeway.testing.fakedata.fixtures.demoapp.demomodule.dom.FakeDataDemoObjectWithAll;
import org.apache.causeway.testing.fakedata.fixtures.demoapp.demomodule.fixturescripts.data.FakeDataDemoObjectWithAll_create_withFakeData;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;

import lombok.Getter;
import lombok.Setter;

@lombok.experimental.Accessors(chain = true)
public class FakeDataDemoObjectWithAll_create3 extends FixtureScript {

    @Getter(onMethod = @__( @Programmatic )) @Setter
    private Integer numberToCreate;
    @Getter(onMethod = @__( @Programmatic )) @Setter
    private Boolean withFakeData;

    @Getter(onMethod = @__( @Programmatic ))
    private List<FakeDataDemoObjectWithAll> demoObjects = _Lists.newArrayList();

    @Override
    protected void execute(final ExecutionContext executionContext) {

        this.defaultParam("numberToCreate", executionContext, 3);
        this.defaultParam("withFakeData", executionContext, true);

        for (int i = 0; i < getNumberToCreate(); i++) {
            final FakeDataDemoObjectWithAll_create_withFakeData fs = new FakeDataDemoObjectWithAll_create_withFakeData().setWithFakeData(withFakeData);
            executionContext.executeChildT(this, fs);
            demoObjects.add(fs.getFakeDataDemoObject());
        }

    }

    @Inject FakeDataService fakeDataService;
}
