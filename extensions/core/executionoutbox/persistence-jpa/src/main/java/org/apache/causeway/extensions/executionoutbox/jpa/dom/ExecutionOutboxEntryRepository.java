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
package org.apache.causeway.extensions.executionoutbox.jpa.dom;

import javax.inject.Named;
import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.extensions.executionoutbox.applib.dom.ExecutionOutboxEntryRepositoryAbstract;

import lombok.Builder;

@Service
@Named(ExecutionOutboxEntryRepositoryAbstract.LOGICAL_TYPE_NAME)
@javax.annotation.Priority(PriorityPrecedence.MIDPOINT)
@Qualifier("Jdo")
public class ExecutionOutboxEntryRepository
extends ExecutionOutboxEntryRepositoryAbstract<ExecutionOutboxEntry> {

    public ExecutionOutboxEntryRepository() {
        super(ExecutionOutboxEntry.class);
    }

    /**
     * for testing only
     */
    @Builder
    ExecutionOutboxEntryRepository(
            Class<ExecutionOutboxEntry> executionLogEntryClass,
            Provider<RepositoryService> repositoryServiceProvider,
            FactoryService factoryService) {
        super(executionLogEntryClass, repositoryServiceProvider, factoryService);
    }

    @Override
    protected ExecutionOutboxEntry newExecutionOutboxEntry() {
        return null;
    }

}
