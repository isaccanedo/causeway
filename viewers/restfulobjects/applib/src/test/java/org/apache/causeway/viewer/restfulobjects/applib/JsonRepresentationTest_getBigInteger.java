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
package org.apache.causeway.viewer.restfulobjects.applib;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.apache.causeway.viewer.restfulobjects.applib.JsonFixture.readJson;

class JsonRepresentationTest_getBigInteger {

    private JsonRepresentation jsonRepresentation;

    @BeforeEach
    public void setUp() throws Exception {
        jsonRepresentation = new JsonRepresentation(readJson("map.json"));
    }

    @Test
    public void happyCase() throws IOException {
        assertThat(jsonRepresentation.getBigInteger("aBigInteger"), is(new BigInteger("12345678901234567890")));
    }

    @Test
    public void happyCaseForFormatJustFits() throws IOException {
        assertThat(jsonRepresentation.getBigInteger("aBigInteger", "big-integer(20)"), is(new BigInteger("12345678901234567890")));
    }

    @Test
    public void invalidFormat() throws IOException {

        var expectMessage = "Value '12345678901234567890' larger than that allowed by format 'big-integer(19)'";

        assertThrows(IllegalArgumentException.class, ()->{
            assertThat(jsonRepresentation.getBigInteger("aBigInteger", "big-integer(19)"), is(new BigInteger("12345678901234567890")));
        }, expectMessage);
    }

    @Test
    public void validFormattedFromPath() throws IOException {
        assertThat(jsonRepresentation.getBigInteger("yetAnotherSubMap.aFormattedBigInteger.value"), is(new BigInteger("123")));
    }

    @Test
    public void invalidFormattedFromPath() throws IOException {
        var expectMessage = "Value '123' larger than that allowed by format 'big-integer(2)'";

        assertThrows(IllegalArgumentException.class, ()->{
            jsonRepresentation.getBigInteger("yetAnotherSubMap.anInvalidFormattedBigInteger.value");
        }, expectMessage);
    }

    @Test
    public void invalidFormattedFromPathButOverridden() throws IOException {
        assertThat(jsonRepresentation.getBigInteger("yetAnotherSubMap.anInvalidFormattedBigInteger.value", "big-integer(3)"), is(new BigInteger("123")));
    }

    @Test
    public void forNonExistent() throws IOException {
        assertThat(jsonRepresentation.getBigInteger("doesNotExist"), is(nullValue()));
    }

    @Test
    public void forNonParseableString() throws IOException {
        var expectMessage = "'aString' is not a biginteger";

        assertThrows(IllegalArgumentException.class, ()->{
            jsonRepresentation.getBigInteger("aString");
        }, expectMessage);
    }

    @Test
    public void forMap() throws IOException {
        var expectMessage = "'aSubMap' is not a biginteger";
        assertThrows(IllegalArgumentException.class, ()->{
            jsonRepresentation.getBigInteger("aSubMap");
        }, expectMessage);
    }

    @Test
    public void forList() throws IOException {
        var expectMessage = "'aSubList' is not a biginteger";
        assertThrows(IllegalArgumentException.class, ()->{
            jsonRepresentation.getBigInteger("aSubList");
        }, expectMessage);
    }

    @Test
    public void forMultipartKey() throws IOException {
        assertThat(jsonRepresentation.getBigInteger("aSubMap.aBigInteger"), is(new BigInteger("12345678901234567890")));
    }

}
