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
package org.apache.causeway.viewer.wicket.ui.test.components.scalars.jodatime;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.core.metamodel.commons.ViewOrEditMode;
import org.apache.causeway.core.metamodel.valuesemantics.temporal.ZonedDateTimeValueSemantics;
import org.apache.causeway.valuetypes.jodatime.integration.valuesemantics.JodaDateTimeValueSemantics;
import org.apache.causeway.viewer.wicket.ui.test.components.scalars.ConverterTester;

import lombok.Getter;
import lombok.Setter;

class JodaDateTimeConverterTest {

    final org.joda.time.DateTime valid = new DateTime(2013, 05, 11, 17, 59, DateTimeZone.forOffsetHours(1));
    ConverterTester<DateTime> converterTester;

    @BeforeEach
    void setUp() throws Exception {
        converterTester = new ConverterTester<org.joda.time.DateTime>(org.joda.time.DateTime.class,
                new JodaDateTimeValueSemantics(),
                new ZonedDateTimeValueSemantics());
        converterTester.setScenario(
                Locale.ENGLISH,
                converterTester.converterForProperty(
                        CustomerWithJodaDateTime.class, "value", ViewOrEditMode.EDITING));
    }

    @Test
    void happy_case() {
        converterTester.assertRoundtrip(valid, "2013-05-11 17:59:00 GMT+01:00");
    }

    @Test
    void when_null() {
        converterTester.assertHandlesEmpty();
    }

    @Test
    void invalid() {
        converterTester.assertConversionFailure("junk", "Not recognised as a java.time.ZonedDateTime: junk");
    }

    // -- SCENARIOS

    @DomainObject
    static class CustomerWithJodaDateTime {
        @Property @Getter @Setter
        private org.joda.time.DateTime value;
    }

}
