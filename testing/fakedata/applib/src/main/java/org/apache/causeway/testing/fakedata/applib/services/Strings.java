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
package org.apache.causeway.testing.fakedata.applib.services;

/**
 * Returns random strings, specifying the number of characters.
 *
 * @since 2.0 {@index}
 */
public class Strings extends AbstractRandomValueGenerator {

    public Strings(final FakeDataService fakeDataService) {
        super(fakeDataService);
    }

    public String upper(final int numChars) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < numChars; i++) {
            buf.append(fake.chars().upper());
        }
        return buf.toString();
    }

    public String fixed(final int numChars) {
        return fake.lorem().javaFakerLorem.fixedString(numChars);
    }

    public String digits(final int numDigits) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < numDigits; i++) {
            buf.append(fake.chars().digit());
        }
        return buf.toString();
    }
}
