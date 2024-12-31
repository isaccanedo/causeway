/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.causeway.persistence.jdo.datanucleus.oid;

import java.util.stream.Stream;

import javax.jdo.identity.ObjectIdentity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.apache.causeway.persistence.jdo.datanucleus.valuetypes.JdoObjectIdentityValueSemantics;

class IdStringifierForObjectIdentity_Test {

    public static Stream<Arguments> roundtrip() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(0),
                Arguments.of(10),
                Arguments.of(Integer.MAX_VALUE),
                Arguments.of(Integer.MIN_VALUE)
        );
    }

    static class Customer {}

    @ParameterizedTest
    @MethodSource()
    void roundtrip(final int value) {

        var entityType = Customer.class;

        var stringifier = JdoObjectIdentityValueSemantics.builder().build();

        var stringified = stringifier.enstring(new ObjectIdentity(entityType, value));
        var parse = stringifier.destring(entityType, stringified);

        Assertions.assertThat(parse.getKeyAsObject()).isEqualTo(value);
        Assertions.assertThat(parse.getTargetClass()).isEqualTo(entityType);

        var decomposed = stringifier.decompose(new ObjectIdentity(entityType, value));
        var composed = stringifier.compose(decomposed);

        Assertions.assertThat(composed.getKeyAsObject()).isEqualTo(value);
        Assertions.assertThat(composed.getTargetClass()).isEqualTo(entityType);
    }

}
