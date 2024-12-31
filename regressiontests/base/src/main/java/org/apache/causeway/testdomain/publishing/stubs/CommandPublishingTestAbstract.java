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
package org.apache.causeway.testdomain.publishing.stubs;

import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.fail;

import org.apache.causeway.applib.services.command.Command;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.exceptions._Exceptions;
import org.apache.causeway.schema.cmd.v2.ActionDto;
import org.apache.causeway.schema.cmd.v2.CommandDto;
import org.apache.causeway.schema.cmd.v2.PropertyDto;
import org.apache.causeway.testdomain.publishing.PublishingTestFactoryAbstract.ChangeScenario;
import org.apache.causeway.testdomain.publishing.PublishingTestFactoryAbstract.VerificationStage;
import org.apache.causeway.testdomain.publishing.subscriber.CommandSubscriberForTesting;
import org.apache.causeway.testdomain.util.CollectionAssertions;
import org.apache.causeway.testdomain.util.kv.KVStoreForTesting;

public abstract class CommandPublishingTestAbstract
extends PublishingTestAbstract {

    @Inject private KVStoreForTesting kvStore;

    @Override
    protected final boolean supportsProgrammaticTesting(final ChangeScenario changeScenario) {
        return false;
    }

    @Override
    protected void given() {
        CommandSubscriberForTesting.clearPublishedCommands(kvStore);
    }

    @Override
    protected void verify(
            final ChangeScenario changeScenario,
            final VerificationStage verificationStage) {
        switch(verificationStage) {

        case FAILURE_CASE:
            assertHasCommandEntries(Can.empty());
            break;
        case PRE_COMMIT:
        case POST_INTERACTION:
            break;
        case POST_COMMIT:

            var command = new Command(UUID.randomUUID());
            var commandDto = new CommandDto();
            commandDto.setInteractionId(command.getInteractionId().toString());

            switch(changeScenario) {
            case PROPERTY_UPDATE:

                var propertyDto = new PropertyDto();
                propertyDto.setLogicalMemberIdentifier(
                        formatPersistenceStandardSpecificLowerCase("testdomain.%s.Book#name"));

                commandDto.setMember(propertyDto);
                command.updater().setCommandDtoAndIdentifier(commandDto);

                assertHasCommandEntries(Can.of(command));
                break;
            case ACTION_INVOCATION:

                var actionDto = new ActionDto();
                actionDto.setLogicalMemberIdentifier(
                        formatPersistenceStandardSpecificLowerCase("testdomain.%s.Book#doubleThePrice"));

                commandDto.setMember(actionDto);
                command.updater().setCommandDtoAndIdentifier(commandDto);

                assertHasCommandEntries(Can.of(command));
                break;
            default:
                throw _Exceptions.unmatchedCase(changeScenario);
            }

            break;
        default:
            // if hitting this, the caller is requesting a verification stage, we are providing no case for
            fail(String.format("internal error, stage not verified: %s", verificationStage));
        }
    }

    // -- HELPER

    private void assertHasCommandEntries(final Can<Command> expectedCommands) {
        var actualCommands = CommandSubscriberForTesting.getPublishedCommands(kvStore);
        CollectionAssertions.assertComponentWiseEquals(
                expectedCommands, actualCommands, this::commandDifference);
    }

    private String commandDifference(final Command a, final Command b) {
        if(!Objects.equals(a.getLogicalMemberIdentifier(), b.getLogicalMemberIdentifier())) {
            return String.format("differing member identifier %s != %s",
                    a.getLogicalMemberIdentifier(), b.getLogicalMemberIdentifier());
        }
        if(!Objects.equals(a.getResult(), b.getResult())) {
            return String.format("differing results %s != %s",
                    a.getResult(), b.getResult());
        }
        return null; // no difference
    }

}
