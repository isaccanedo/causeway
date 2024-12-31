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
package org.apache.causeway.commons.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.context.annotation.Primary;

import lombok.experimental.UtilityClass;

/**
 * <h1>- internal use only -</h1>
 * <p>
 * A collection of commonly used constants.
 * </p>
 * <p>
 * <b>WARNING</b>: Do <b>NOT</b> use any of the classes provided by this package! <br/>
 * These may be changed or removed without notice!
 * </p>
 *
 * @since 2.0
 */
@UtilityClass
public final class _Constants {

    /**
     * Convenient e.g. for reflective invocation
     */
    public final Object[] emptyObjects = new Object[0];

    /**
     * Convenient e.g. for reflective invocation
     */
    public final Class<?>[] emptyClasses = new Class[0];

    /**
     * Convenient e.g. for reflective invocation
     */
    public final Class<?>[] classesOfObject = new Class[] { Object.class };

    /**
     * Convenient e.g. for toArray conversions
     */
    public final String[] emptyStringArray = new String[0];

    /**
     * empty array of byte
     */
    public final byte[] emptyBytes = new byte[0];

    /**
     * empty array of Annotation
     */
    public final Annotation[] emptyAnnotations = new Annotation[0];

    public final Method[] nonFinalObjectMethods = nonFinalObjectMethods();
    private final Method[] nonFinalObjectMethods() {
        try {
            return new Method[] {
                Object.class.getMethod("toString"),
                Object.class.getMethod("hashCode"),
                Object.class.getMethod("equals", classesOfObject)};
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writer that does nothing
     */
    public final Writer nopWriter = new Writer() {
        @Override public void write(final char[] cbuf, final int off, final int len) throws IOException { }
        @Override public void flush() throws IOException { }
        @Override public void close() throws IOException { }
    };

    /**
     * OutputStream that does nothing
     */
    public final OutputStream nopOutputStream = new OutputStream() {
        @Override public void write(final int b) throws IOException { }
    };

    /**
     * PrintStream that does nothing
     */
    public final PrintStream nopPrintStream = new PrintStream(nopOutputStream);

    @Primary private final class PrimaryAnnotated {}
    public final Primary ANNOTATION_PRIMARY = PrimaryAnnotated.class.getAnnotation(Primary.class);

}
