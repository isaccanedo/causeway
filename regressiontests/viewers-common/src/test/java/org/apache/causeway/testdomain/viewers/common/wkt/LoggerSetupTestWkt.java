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
package org.apache.causeway.testdomain.viewers.common.wkt;

import org.apache.wicket.page.PartialPageUpdate;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.apache.causeway.core.config.presets.CausewayPresets;

@SpringBootTest(
        classes = {
                LoggerSetupTestWkt.Config.class,
        },
        properties = {
        })
@TestPropertySource(CausewayPresets.SilenceWicket)
public class LoggerSetupTestWkt {

    @Configuration
    static class Config {

    }

    @Test
    void slf4jLoggers_shouldBeBridgedToWorkWithLog4j2() {

        var logger = LoggerFactory.getLogger(PartialPageUpdate.class);
        assertFalse(logger.isWarnEnabled());

    }

}
