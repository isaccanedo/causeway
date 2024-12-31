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
package org.apache.causeway.valuetypes.asciidoc.builder.test.ast;

import java.io.IOException;

import org.asciidoctor.ast.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.causeway.valuetypes.asciidoc.builder.AsciiDocFactory.cell;
import static org.apache.causeway.valuetypes.asciidoc.builder.AsciiDocFactory.doc;
import static org.apache.causeway.valuetypes.asciidoc.builder.AsciiDocFactory.headCell;
import static org.apache.causeway.valuetypes.asciidoc.builder.AsciiDocFactory.table;

class SimpleTableTest extends AbstractAsciiDocWriterTest {

    private Document doc;

    @BeforeEach
    void setUp() throws Exception {
        doc = doc();
        super.adocSourceResourceLocation = "table-simple.adoc";
        super.debugEnabled = false;
    }

    @Test
    void testTable() throws IOException {

        doc.setTitle("SimpleTableTest");

        // assert that we have a blank line before the title block starts

        var table = table(doc);

        headCell(table, 0, 0, "Col-1");
        headCell(table, 0, 1, "Col-2");
        headCell(table, 0, 2, "Col-3");

        cell(table, 0, 0, "1-1");
        cell(table, 0, 1, "1-2");
        cell(table, 0, 2, "1-3");

        cell(table, 1, 0, "2-1");
        cell(table, 1, 1, "2-2");
        cell(table, 1, 2, "2-3");

        assertDocumentIsCorrectlyWritten(doc);
    }

}
