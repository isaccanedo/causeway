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
package org.apache.causeway.commons.internal.testing;

import java.io.Serializable;

import org.apache.causeway.commons.internal.assertions._Assert;
import org.apache.causeway.commons.internal.base._Casts;
import org.apache.causeway.commons.internal.resources._Serializables;

import lombok.SneakyThrows;

/**
 * in-memory serialization round-tripping
 * @since 2.0
 *
 */
public class _SerializationTester {

    @SneakyThrows
    public static <T extends Serializable> T roundtrip(final T object) {
        var bytes = _Serializables.write(object);
        return _Casts.uncheckedCast(
                _Serializables.read(object.getClass(), bytes));
    }

    @SneakyThrows
    public static <T extends Serializable> void assertEqualsOnRoundtrip(final T object) {
        T afterRoundtrip = roundtrip(object);
        _Assert.assertEquals(object, afterRoundtrip);
    }

    @SneakyThrows
    public static void selftest() {
        String afterRoundtrip = roundtrip("Hello World!");
        _Assert.assertEquals("Hello World!", afterRoundtrip);
        assertEqualsOnRoundtrip("Hello World!");
    }

}
