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
package org.apache.causeway.testdomain.publishing.jdo;

import javax.inject.Inject;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import org.apache.causeway.core.config.presets.CausewayPresets;
import org.apache.causeway.testdomain.conf.Configuration_usingJdo;
import org.apache.causeway.testdomain.jdo.HasPersistenceStandardJdo;
import org.apache.causeway.testdomain.publishing.PublishingTestFactoryAbstract;
import org.apache.causeway.testdomain.publishing.PublishingTestFactoryJdo;
import org.apache.causeway.testdomain.publishing.conf.Configuration_usingEntityPropertyChangePublishing;
import org.apache.causeway.testdomain.publishing.stubs.PropertyPublishingTestAbstract;

@SpringBootTest(
        classes = {
                Configuration_usingJdo.class,
                Configuration_usingEntityPropertyChangePublishing.class,
                PublishingTestFactoryJdo.class,
                //XrayEnable.class
        },
        properties = {
                "logging.level.org.apache.causeway.applib.services.publishing.log.EntityPropertyChangeLogger=DEBUG",
                "logging.level.org.apache.causeway.testdomain.util.rest.KVStoreForTesting=DEBUG",
                "logging.level.org.apache.causeway.persistence.jdo.integration.changetracking.JdoLifecycleListener=DEBUG",
                "logging.level.org.apache.causeway.core.transaction.changetracking.EntityChangeTrackerDefault=DEBUG",
        })
@TestPropertySource({
    CausewayPresets.UseLog4j2Test
})
class JdoPropertyPublishingTest
extends PropertyPublishingTestAbstract
implements HasPersistenceStandardJdo {

    @Inject private PublishingTestFactoryJdo testFactory;

    @Override
    protected PublishingTestFactoryAbstract getTestFactory() {
        return testFactory;
    }

}
