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
package org.apache.causeway.testdomain.entitychangetracking.jdo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.causeway.commons.internal.collections._Lists;
import org.apache.causeway.core.config.presets.CausewayPresets;
import org.apache.causeway.testdomain.conf.Configuration_usingJdo;
import org.apache.causeway.testdomain.jdo.JdoInventoryManager;
import org.apache.causeway.testdomain.jdo.entities.JdoBook;
import org.apache.causeway.testdomain.jdo.entities.JdoProduct;
import org.apache.causeway.testdomain.publishing.conf.Configuration_usingEntityChangesPublishing;
import org.apache.causeway.testdomain.publishing.conf.Configuration_usingEntityPropertyChangePublishing;
import org.apache.causeway.testdomain.publishing.subscriber.EntityPropertyChangeSubscriberForTesting;
import org.apache.causeway.testdomain.util.interaction.InteractionBoundaryProbe;
import org.apache.causeway.testdomain.util.interaction.InteractionTestAbstract;

import lombok.val;

@SpringBootTest(
        classes = {
                Configuration_usingJdo.class,
                Configuration_usingEntityPropertyChangePublishing.class,
                Configuration_usingEntityChangesPublishing.class,
                InteractionBoundaryProbe.class,
        },
        properties = {
                "logging.level.org.apache.causeway.testdomain.util.rest.KVStoreForTesting=DEBUG",
                "logging.level.org.apache.causeway.persistence.jdo.datanucleus.persistence.CausewayTransactionJdo=DEBUG"
        })
@TestPropertySource({
    CausewayPresets.SilenceWicket
    ,CausewayPresets.UseLog4j2Test
})
class JdoEntityChangePublishingTest extends InteractionTestAbstract {

    //@Inject protected FixtureScripts fixtureScripts;

    @BeforeEach
    void setUp() {
        System.err.println("===BEFORE SETUP");

        // cleanup
        EntityPropertyChangeSubscriberForTesting.clearPropertyChangeEntries(kvStoreForTesting);

        // given
        //fixtureScripts.runPersona(JdoTestDomainPersona.InventoryWith1Book);

        // each test runs in its own interaction context (check)
        val testRunNr = kvStoreForTesting.incrementCounter(JdoEntityChangePublishingTest.class, "test-run");
        assertEquals(testRunNr, InteractionBoundaryProbe.totalInteractionsStarted(kvStoreForTesting));

        assertJdoBookCreatePropertyChanges();

        System.err.println("===AFTER SETUP");
    }

    @AfterEach
    void tearDown() {
        System.err.println("===BEFORE TEARDOWN");

        // cleanup
        //fixtureScripts.runPersona(JdoTestDomainPersona.PurgeAll);

        assertJdoBookDeletePropertyChanges();

        System.err.println("===AFTER TEARDOWN");
    }

    @Test
    void wrapperInvocation_shouldSpawnSingleTransaction() {

        // given
        val book = getBookSample();
        val inventoryManager = factoryService.create(JdoInventoryManager.class);

        assertEquals(99., book.getPrice(), 1E-3);

        System.err.println("=1==BEFORE  TX");

        // spawns its own transactional boundary (check)
        val product = assertTransactional(
                ()->wrapper.wrap(inventoryManager).updateProductPrice(book, 12.));

        System.err.println("=1==AFTER  TX");

        assertEquals(12., product.getPrice(), 1E-3);

        assertNoChangedObjectsPending();
        assertJdoBookPriceChangePropertyChanges();

    }

    @Test
    void actionInteraction_shouldSpawnSingleTransaction() {

        // given
        val book = getBookSample();

        assertEquals(99., book.getPrice(), 1E-3);

        System.err.println("=2==BEFORE  TX");

        // spawns its own transactional boundary (check)
        val product = (JdoProduct) assertTransactional(
                ()->invokeAction(JdoInventoryManager.class, "updateProductPrice", _Lists.of(book, 12.)));

        System.err.println("=2==AFTER  TX");

        assertEquals(12., product.getPrice(), 1E-3);

        assertNoChangedObjectsPending();
        assertJdoBookPriceChangePropertyChanges();

    }

    // -- HELPER

    private JdoBook getBookSample() {

        System.err.println("===BEFORE BOOK");

        // spawns its own transactional boundary (check)
        val books = assertTransactional(()->repositoryService.allInstances(JdoBook.class));
        assertEquals(1, books.size());
        val book = books.listIterator().next();

        assertNoPropertyChanges(); // query only, no entity changes expected

        System.err.println("===AFTER BOOK");
        return book;
    }

}
