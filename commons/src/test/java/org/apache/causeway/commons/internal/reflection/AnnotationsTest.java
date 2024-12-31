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
package org.apache.causeway.commons.internal.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.util.ClassUtils;

import org.apache.causeway.commons.internal._Constants;

import lombok.Getter;
import lombok.Setter;

class AnnotationsTest {

    // -- SCENARIOS

    static abstract class AbstractBase {
        @DisplayName("hi") void action(){}
    }

    static class Concrete extends AbstractBase {

    }

    static class ConcreteOverride extends AbstractBase {
        @Override
        void action(){}
    }

    static interface PrimitiveBooleanHolder {
        @Order(42)
        boolean isReadWriteProperty();
        void setReadWriteProperty(boolean c);
    }

    static class PrimitiveBooleanEntity implements PrimitiveBooleanHolder {
        @Order(43)
        @Getter @Setter
        private boolean readWriteProperty;
    }

    // -- TESTS

    @Test
    void inhertitedAnnotation() {

        final long methodCount =
                _Reflect.streamAllMethods(Concrete.class, true)
                .filter(m->m.getName().equals("action"))
                // using filter over peek here, because peek is unreliable with 'count()' terminal
                .filter(m->{
                    var syn = _Annotations.synthesize(m, DisplayName.class);
                    assertNotNull(syn);
                    assertTrue(syn.isPresent());
                    assertEquals("hi", syn.get().value());
                    return true;
                })
                .count();

        assertEquals(1, methodCount);

    }

    @Test
    void inhertitedAnnotationWhenOverride() {

        final long methodCount =
                _Reflect.streamAllMethods(ConcreteOverride.class, true)
                .filter(m->m.getName().equals("action"))
                // using filter over peek here, because peek is unreliable with 'count()' terminal
                .filter(m->{
                    var syn = _Annotations.synthesize(m, DisplayName.class);
                    assertNotNull(syn);
                    assertTrue(syn.isPresent());
                    assertEquals("hi", syn.get().value());
                    return true;
                })
                .count();

        assertEquals(2, methodCount);

    }

    @Test
    void inhertitedAnnotationWhenOverrideOnBooleanProperty() {
        var getter = ClassUtils
                .getMethod(PrimitiveBooleanEntity.class, "isReadWriteProperty", _Constants.emptyClasses);

        assertEquals(43, _Annotations.synthesize(getter, Order.class).get().value());
    }

}
