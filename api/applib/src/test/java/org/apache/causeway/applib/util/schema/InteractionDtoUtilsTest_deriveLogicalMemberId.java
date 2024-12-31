/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.causeway.applib.util.schema;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.causeway.applib.services.bookmark.Bookmark;

class InteractionDtoUtilsTest_deriveLogicalMemberId {

    @Test
    public void happy_case() throws Exception {
        String s = InteractionDtoUtils.deriveLogicalMemberId(
                Bookmark.forLogicalTypeNameAndIdentifier("customer.Order", "1234"),
                "com.mycompany.customer.Order#placeOrder");
        assertThat(s, is(equalTo("customer.Order#placeOrder")));
    }
}