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

import java.math.BigInteger;
import java.util.List;

import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.SemanticsOf;

@Named("testdomain.RoSpecSampler")
@DomainService
@javax.annotation.Priority(PriorityPrecedence.EARLY)
public class RoSpecSampler {

    // -- VOID

    @Action
    public void voidResult() {
    }

    // -- STRING

    @Action
    public String string() {
        return "aString";
    }

    @Action(semantics = SemanticsOf.SAFE)
    public String stringSafe() {
        return "aSafeString";
    }

    @Action
    public String stringNull() {
        return null;
    }

    // -- STRING ARRAY

    @Action
    public String[] stringArray() {
        return new String[] {"Hello", "World!"};
    }

    @Action
    public String[] stringArrayEmpty() {
        return new String[0];
    }

    @Action
    public String[] stringArrayNull() {
        return null;
    }

    // -- STRING LIST

    @Action
    public List<String> stringList() {
        return List.of("Hello", "World!");
    }

    @Action
    public List<String> stringListEmpty() {
        return List.of();
    }

    @Action
    public List<String> stringListNull() {
        return null;
    }

    // -- INT

    @Action
    public int integerPrimitive() {
        return 123;
    }

    @Action
    public Integer integer() {
        return 123;
    }

    @Action
    public Integer integerNull() {
        return null;
    }

    // -- BIG INT

    @Action
    public BigInteger bigInteger() {
        return BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
    }

    @Action
    public BigInteger bigIntegerNull() {
        return null;
    }

    @Action
    public List<BigInteger> bigIntegerList() {
        return List.of(BigInteger.ZERO, bigInteger());
    }

    // -- CUSTOMER

    @Action
    public Customer customer() {
        return new Customer("Hello World!", 22);
    }

    @Action
    public Customer customerNull() {
        return null;
    }

    // -- CUSTOMER LIST

    @Action
    public List<Customer> customerList() {
        return List.of(
                new Customer("Alice", 22),
                new Customer("Bob", 33));
    }

    @Action
    public List<Customer> customerListEmpty() {
        return List.of();
    }

    @Action
    public List<Customer> customerListNull() {
        return null;
    }

    // -- COMPOSITE

    @Action
    public List<BigComplex> complexList() {
        return List.of(BigComplex.zero(), BigComplex.of("2.1", "-4.3"));
    }

    @Action
    public BigComplex complexAdd(final String are, final String aim, final String bre, final String bim) {
        return BigComplex.of(are, aim).add(BigComplex.of(bre, bim));
    }

}
