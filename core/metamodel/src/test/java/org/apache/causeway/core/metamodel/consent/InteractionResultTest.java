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
package org.apache.causeway.core.metamodel.consent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.core.metamodel.consent.Consent.VetoReason;

class InteractionResultTest {

    private InteractionResult result;

    @BeforeEach
    public void setUp() throws Exception {
        result = new InteractionResult(null);
    }

    @AfterEach
    public void tearDown() throws Exception {
        result = null;
    }

    @Test
    public void shouldHaveNullReasonWhenJustInstantiated() {
        assertEquals(null, extractReason(result));
    }

    @Test
    public void shouldBeEmptyWhenJustInstantiated() {
        assertFalse(result.isVetoing());
        assertTrue(result.isNotVetoing());
    }

    @Test
    public void shouldHaveNonNullReasonWhenAdvisedWithNonNull() {
        result.advise(vetoReason("foo"), InteractionAdvisor.forTesting());
        assertEquals("foo", extractReason(result));
    }

    @Test
    public void shouldConcatenateAdviseWhenAdvisedWithNonNull() {
        result.advise(vetoReason("foo"), InteractionAdvisor.forTesting());
        result.advise(vetoReason("bar"), InteractionAdvisor.forTesting());
        assertEquals("foo; bar", extractReason(result));
    }

    @Test
    public void shouldNotBeEmptyWhenAdvisedWithNonNull() {
        result.advise(vetoReason("foo"), InteractionAdvisor.forTesting());
        assertTrue(result.isVetoing());
        assertFalse(result.isNotVetoing());
    }

    @Test
    public void shouldBeEmptyWhenAdvisedWithNull() {
        result.advise(null, InteractionAdvisor.forTesting());
        assertTrue(result.isNotVetoing());
        assertFalse(result.isVetoing());
        assertEquals(null, extractReason(result));
    }

    // -- HELPER

    static Consent.VetoReason vetoReason(final String reasonString) {
        return Consent.VetoReason.explicit(reasonString);
    }

    static String extractReason(final InteractionResult result) {
        return result.getReason().map(VetoReason::string).orElse(null);
    }

}
