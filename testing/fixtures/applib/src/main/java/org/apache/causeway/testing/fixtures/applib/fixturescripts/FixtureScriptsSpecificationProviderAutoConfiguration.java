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
package org.apache.causeway.testing.fixtures.applib.fixturescripts;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.core.config.CausewayConfiguration;

/**
 * Provides a fallback implementation of {@link FixtureScriptsSpecificationProvider} if none has been provided explicitly by the application itself.
 *
 * @since 2.0 {@index}
 */
@AutoConfigureOrder(PriorityPrecedence.LATE)
@Configuration
public class FixtureScriptsSpecificationProviderAutoConfiguration  {

    /**
     * Returns an implementation of {@link FixtureScriptsSpecificationProvider} that
     * uses configuration properties under <code>causeway.testing.fixtures.fixture-scripts-specification</code>.
     *
     * @return
     */
    @Bean("causeway.testing.fixtures.FixtureScriptsSpecificationProviderDefault")
    @ConditionalOnMissingBean(FixtureScriptsSpecificationProvider.class)
    @Qualifier("Default")
    FixtureScriptsSpecificationProvider fixtureScriptsSpecificationProvider(final CausewayConfiguration causewayConfiguration) {

        var fixturesConfig = causewayConfiguration.getTesting().getFixtures().getFixtureScriptsSpecification();
        var builder = builderFrom(fixturesConfig);

        builder.with(FixtureScripts.NonPersistedObjectsStrategy.valueOf(fixturesConfig.getNonPersistedObjectsStrategy().name()));
        builder.with(FixtureScripts.MultipleExecutionStrategy.valueOf(fixturesConfig.getMultipleExecutionStrategy().name()));
        builder.withRecreate((Class) fixturesConfig.getRecreate());
        builder.withRunScriptDefault((Class) fixturesConfig.getRunScriptDefault());

        return builder::build;
    }

    private static FixtureScriptsSpecification.Builder builderFrom(CausewayConfiguration.Testing.Fixtures.FixtureScriptsSpecification fixturesConfig) {
        var contextClass = fixturesConfig.getContextClass();
        if(contextClass != null) {
            return FixtureScriptsSpecification.builder(contextClass);
        }
        var packagePrefix = fixturesConfig.getPackagePrefix(); // could be null; this is legitimate
        return FixtureScriptsSpecification.builder(packagePrefix);
    }

}
