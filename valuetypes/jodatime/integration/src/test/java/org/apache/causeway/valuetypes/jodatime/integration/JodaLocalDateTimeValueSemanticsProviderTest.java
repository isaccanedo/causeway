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
package org.apache.causeway.valuetypes.jodatime.integration;

import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.causeway.applib.value.semantics.ValueSemanticsAbstract;
import org.apache.causeway.core.metamodel.valuesemantics.temporal.LocalDateTimeValueSemantics;
import org.apache.causeway.valuetypes.jodatime.integration.valuesemantics.JodaLocalDateTimeValueSemantics;

public class JodaLocalDateTimeValueSemanticsProviderTest {

    private JodaLocalDateTimeValueSemantics valueSemantics;

    @BeforeEach
    public void setUp() throws Exception {

        var delegate = new LocalDateTimeValueSemantics();

        valueSemantics = new JodaLocalDateTimeValueSemantics() {
            @Override
            public ValueSemanticsAbstract<java.time.LocalDateTime> getDelegate() {
                return delegate;
            }
        };
    }

    @Test
    public void roundtrip() throws Exception {

        final LocalDateTime t0 = LocalDateTime.now();

        var encoded = valueSemantics.decompose(t0);
        final LocalDateTime t1 = valueSemantics.compose(encoded);

        assertThat(t0, is(equalTo(t1)));
    }

}