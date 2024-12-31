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
package org.apache.causeway.core.runtimeservices.email;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class EmailServiceDefaultTest_notEmpty {

    @Test
    void when_not_empty() throws Exception {
        assertThat(EmailServiceDefault.notEmpty(new String[] { "joey@tribiani.com", "rachel@green.com" }), is(true));
        assertThat(EmailServiceDefault.notEmpty(new String[] { "rachel@green.com" }), is(true));
    }

    @Test
    void when_null() throws Exception {
        assertThat(EmailServiceDefault.notEmpty(null), is(false));
    }

    @Test
    void when_empty() throws Exception {
        assertThat(EmailServiceDefault.notEmpty(new String[]{}), is(false));
    }

}
