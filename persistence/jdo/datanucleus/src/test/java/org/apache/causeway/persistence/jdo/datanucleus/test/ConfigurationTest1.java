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
package org.apache.causeway.persistence.jdo.datanucleus.test;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = {
                ConfigurationExample1.class
        },
        properties = {
                "datanucleus.cache.level2.mode=ENABLE_SELECTIVE",
                "datanucleus.cache.level2.type=none",
                "datanucleus.identifier.case=MixedCase",
                "datanucleus.persistenceByReachabilityAtCommit=false",
                "datanucleus.schema.autoCreateAll=true",
                "datanucleus.schema.validateAll=false",
                "datanucleus.schema.validateConstraints=true",
                "datanucleus.schema.validateTables=true",
                
                "javax.jdo.PersistenceManagerFactoryClass=org.datanucleus.api.jdo.JDOPersistenceManagerFactory",
                "javax.jdo.option.ConnectionDriverName=org.h2.Driver",
                "javax.jdo.option.ConnectionPassword=",
                "javax.jdo.option.ConnectionURL=jdbc:h2:mem:test",
                "javax.jdo.option.ConnectionUserName=sa",
       }
)
class ConfigurationTest1 {

    @Test 
    void contextLoads() {
    }
    
}
