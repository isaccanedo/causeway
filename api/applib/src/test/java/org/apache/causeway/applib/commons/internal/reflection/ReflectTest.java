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
package org.apache.causeway.applib.commons.internal.reflection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.commons.internal.collections._Sets;
import org.apache.causeway.commons.internal.reflection._Reflect.InterfacePolicy;

import static org.apache.causeway.commons.internal.reflection._Reflect.getAnnotation;
import static org.apache.causeway.commons.internal.reflection._Reflect.streamAllMethods;
import static org.apache.causeway.commons.internal.reflection._Reflect.streamTypeHierarchy;

class ReflectTest {

    @Test
    void typeHierarchy() {

        Class<?> type = ReflectSampleForTesting.Nested.class;

        var typeSet = streamTypeHierarchy(type, InterfacePolicy.EXCLUDE)
                .map(Class::getName)
                .collect(Collectors.toSet());

        assertSetContainsAll(_Sets.<String>of(
                    "org.apache.causeway.applib.commons.internal.reflection.ReflectSampleForTesting$Nested",
                    "java.lang.Object"),
                typeSet);
    }

    @Test
    void typeHierarchyAndInterfaces() {

        Class<?> type = ReflectSampleForTesting.Nested.class;

        var typeSet = streamTypeHierarchy(type, InterfacePolicy.INCLUDE)
                .map(Class::getName)
                .collect(Collectors.toSet());

        assertSetContainsAll(_Sets.<String>of(
                    "org.apache.causeway.applib.commons.internal.reflection.ReflectSampleForTesting$NestedInterface",
                    "org.apache.causeway.applib.commons.internal.reflection.ReflectSampleForTesting$Nested",
                    "java.lang.Object"),
                typeSet);

    }

    @Test
    void allMethods() {

        Class<?> type = ReflectSampleForTesting.Nested.class;

        var typeSet = streamAllMethods(type, true)
                .map(m->m.toString())
                .collect(Collectors.toSet());

        assertSetContainsAll(_Sets.<String>of(
                "public abstract void org.apache.causeway.applib.commons.internal.reflection.ReflectSampleForTesting$NestedInterface.sayHello()",
                "public void org.apache.causeway.applib.commons.internal.reflection.ReflectSampleForTesting$Nested.sayHello()"),
            typeSet);

    }

    @Programmatic
    public void annotationLookupTestMethod(final String username, final List<String> roles) {
    }

    @Test
    void annotationLookup() throws NoSuchMethodException, SecurityException {

        Class<?> type = getClass();
        Method method = type.getMethod("annotationLookupTestMethod", new Class[] {String.class, List.class});

        Programmatic annot = getAnnotation(method, Programmatic.class, true, true);

        assertNotNull(annot);
    }

    static class A extends I.B {}
    static interface I {
        static class B implements I {}
    }

    @Test
    void typeHierarchyAndInterfaces2() {

        Class<?> type = A.class;

        var typeSet = streamTypeHierarchy(type, InterfacePolicy.INCLUDE)
                .map(t->t.getSimpleName())
                .collect(Collectors.toCollection(TreeSet::new));

        assertEquals(_Sets.<String>ofSorted(
                "A",
                "B",
                "I",
                "Object"), typeSet);

    }

    // -- HELPER

    private static void assertSetContainsAll(final Set<String> shouldContain, final Set<String> actuallyContains) {
        assertTrue(_Sets.minus(shouldContain, actuallyContains).isEmpty());
    }

}
