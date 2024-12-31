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
package org.apache.causeway.testing.unittestsupport.applib.dom.pojo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import org.apache.causeway.testing.unittestsupport.applib.dom.pojo.holders.JodaLocalDateHolder;
import org.apache.causeway.testing.unittestsupport.applib.dom.pojo.holders.JodaLocalDateTimeHolder;
import org.apache.causeway.testing.unittestsupport.applib.dom.pojo.holders.JodaLocalTimeHolder;

public class PojoTester_datatypes_joda_Test {

    @Test
    public void exercise_local_date() {

        // given
        var holder = new JodaLocalDateHolder();
        Assertions.assertThat(holder).extracting(JodaLocalDateHolder::getSomeLocalDate).isNull();

        // when
        PojoTester.create()
                .exercise(holder);

        // then
        Assertions.assertThat(holder).extracting(JodaLocalDateHolder::getSomeLocalDate).isNotNull();
        Assertions.assertThat(holder.getCounter()).isGreaterThan(0);
    }

    @Test
    public void exercise_local_date_broken() {

        // given
        var holder = new JodaLocalDateHolder().butBroken();

        // when
        Assertions.assertThatThrownBy(() -> {
            PojoTester.create()
                        .exercise(holder);
        }).isInstanceOf(AssertionFailedError.class);
    }

    @Test
    public void exercise_local_date_time() {

        // given
        var holder = new JodaLocalDateTimeHolder();
        Assertions.assertThat(holder).extracting(JodaLocalDateTimeHolder::getSomeLocalDateTime).isNull();

        // when
        PojoTester.create()
                .exercise(holder);

        // then
        Assertions.assertThat(holder).extracting(JodaLocalDateTimeHolder::getSomeLocalDateTime).isNotNull();
        Assertions.assertThat(holder.getCounter()).isGreaterThan(0);
    }

    @Test
    public void exercise_local_date_time_broken() {

        // given
        var holder = new JodaLocalDateTimeHolder().butBroken();

        // when
        Assertions.assertThatThrownBy(() -> {
            PojoTester.create()
                        .exercise(holder);
        }).isInstanceOf(AssertionFailedError.class);
    }

    @Test
    public void exercise_local_time() {

        // given
        var holder = new JodaLocalTimeHolder();
        Assertions.assertThat(holder).extracting(JodaLocalTimeHolder::getLocalTime).isNull();

        // when
        PojoTester.create()
                .exercise(holder);

        // then
        Assertions.assertThat(holder).extracting(JodaLocalTimeHolder::getLocalTime).isNotNull();
        Assertions.assertThat(holder.getCounter()).isGreaterThan(0);
    }

    @Test
    public void exercise_local_time_broken() {

        // given
        var holder = new JodaLocalTimeHolder().butBroken();

        // when
        Assertions.assertThatThrownBy(() -> {
            PojoTester.create()
                        .exercise(holder);
        }).isInstanceOf(AssertionFailedError.class);
    }

}
