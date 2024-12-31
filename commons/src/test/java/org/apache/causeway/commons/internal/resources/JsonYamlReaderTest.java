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
package org.apache.causeway.commons.internal.resources;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.causeway.commons.io.DataSource;
import org.apache.causeway.commons.io.JsonUtils;
import org.apache.causeway.commons.io.YamlUtils;

import lombok.Data;

class JsonYamlReaderTest {

    @Data
    public static class Customer {
        private String firstName;
        private String lastName;
        private int age;
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void loadCustomerFromJson() throws JsonParseException, JsonMappingException, IOException {
        var customer = JsonUtils.tryRead(Customer.class, DataSource.ofResource(this.getClass(), "customer.json"))
                .ifFailureFail()
                .getValue()
                .orElse(null);
        assertCustomerIsJohnDoe(customer);
    }

    @Test
    void loadCustomerFromYaml() {
        var customer = YamlUtils.tryRead(Customer.class, DataSource.ofResource(this.getClass(), "customer.yml"))
                .ifFailureFail()
                .getValue()
                .orElse(null);
        assertCustomerIsJohnDoe(customer);
    }

    // -- HELPER

    private void assertCustomerIsJohnDoe(final Customer customer) {
        assertNotNull(customer);
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals(20, customer.getAge());
    }
}
