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
package org.apache.causeway.applib.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.causeway.applib.annotation.Where;

class EnumsTest {

    @Test
    public void getFriendlyNameOf() {
        assertThat(Enums.getFriendlyNameOf(Where.ANYWHERE), is("Anywhere"));
        assertThat(Enums.getFriendlyNameOf(Where.ALL_TABLES), is("All Tables"));
    }

    @Test
    public void getEnumNameFromFriendly() {
        assertThat(Enums.getEnumNameFromFriendly("Anywhere"), is("ANYWHERE"));
        assertThat(Enums.getEnumNameFromFriendly("All Tables"), is("ALL_TABLES"));
    }

    @Test
    void enumTitle() {
        assertThat(enumTitle("FOO"), is("Foo"));
        assertThat(enumTitle("FOO_BAR"), is("Foo Bar"));
    }
    private static String enumTitle(final String enumName) {
        return Enums.getFriendlyNameOf(enumName);
    }

    @Test
    void enumDeTitle() {
        assertThat(enumDeTitle("Foo"), is("FOO"));
        assertThat(enumDeTitle("Foo Bar"), is("FOO_BAR"));
    }
    private static String enumDeTitle(final String enumFriendlyName) {
        return Enums.getEnumNameFromFriendly(enumFriendlyName);
    }

}
