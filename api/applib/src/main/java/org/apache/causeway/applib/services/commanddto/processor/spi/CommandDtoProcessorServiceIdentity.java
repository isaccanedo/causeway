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
package org.apache.causeway.applib.services.commanddto.processor.spi;

import javax.inject.Named;

import org.springframework.stereotype.Service;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.schema.cmd.v2.CommandDto;

/**
 * At least one implementation is required.
 *
 * @since 1.x {@index}
 */
@Service
@Named(CommandDtoProcessorServiceIdentity.LOGICAL_TYPE_NAME)
@javax.annotation.Priority(PriorityPrecedence.LAST)
public class CommandDtoProcessorServiceIdentity implements CommandDtoProcessorService {

    static final String LOGICAL_TYPE_NAME = CausewayModuleApplib.NAMESPACE + ".CommandDtoProcessorServiceIdentity";

    @Override
    public CommandDto process(final Object domainObject, final CommandDto commandDto) {
        return commandDto;
    }
}
