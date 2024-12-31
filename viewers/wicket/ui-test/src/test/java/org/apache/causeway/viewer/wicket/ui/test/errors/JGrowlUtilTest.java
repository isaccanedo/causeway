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
package org.apache.causeway.viewer.wicket.ui.test.errors;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.causeway.viewer.wicket.ui.errors.JGrowlUtil;

class JGrowlUtilTest {

    @Test
    void testEscape() throws Exception {
        assertThat(JGrowlUtil
                .escape(
                        "double quotes \" and single quotes ' and <p>markup</p>"),
                equalTo(
                        "double quotes ' and single quotes ' and &lt;p&gt;markup&lt;/p&gt;"));
    }

    @Test
    void testNewlineEscape() throws Exception {
        assertThat(JGrowlUtil
                .escape(
                        "a\n\rb"),
                equalTo(
                        "a<br/>b"));
    }

}