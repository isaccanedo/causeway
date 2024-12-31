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
package org.apache.causeway.testdomain.rospec;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.causeway.commons.internal.assertions._Assert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Example (composite) type for testing.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor(staticName = "of")
public final class BigComplex {

    private BigDecimal re;
    private BigDecimal im;

    public static BigComplex zero() {
        return BigComplex.of(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public static BigComplex of(final String re, final String im) {
        return BigComplex.of(new BigDecimal(re), new BigDecimal(im));
    }

    public BigComplex add(final BigComplex other) {
        return BigComplex.of(
                this.re.add(other.re),
                this.im.add(other.im));
    }

    public BigComplex subtract(final BigComplex other) {
        return BigComplex.of(
                this.re.subtract(other.re),
                this.im.subtract(other.im));
    }

    @Override
    public boolean equals(final Object obj) {
        if(!(obj instanceof BigComplex)) {
            return false;
        }
        var other = (BigComplex) obj;
        return this.re.compareTo(other.re) == 0
                && this.im.compareTo(other.im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }

    public static void assertEquals(final BigComplex a, final BigComplex b) {
        _Assert.assertEquals(a.re.toPlainString(), b.re.toPlainString());
        _Assert.assertEquals(a.im.toPlainString(), b.im.toPlainString());
    }

}
