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
import java.nio.charset.StandardCharsets;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.ast.Document;
import org.junit.jupiter.api.Test;

import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.commons.internal.base._Text;
import org.apache.causeway.commons.io.TextUtils;
import org.apache.causeway.valuetypes.asciidoc.builder.AsciiDocWriter;

abstract class AbstractAsciiDocWriterTest {

    protected String adocSourceResourceLocation;
    protected boolean debugEnabled;
    protected boolean skipAsciidocjComplianceTest;

    protected void assertDocumentIsCorrectlyWritten(final Document documentUnderTest) {

        final String sourceUnderTest = AsciiDocWriter.toString(documentUnderTest);

        if(debugEnabled) {
            System.out.println("======= Generated Adoc Source =======");
            System.out.println(sourceUnderTest);
            System.out.println("=====================================");
        }

        _Text.assertTextEquals(
                TextUtils.readLinesFromResource(this.getClass(), "/approved/" + adocSourceResourceLocation, StandardCharsets.UTF_8),
                sourceUnderTest);
    }

    protected void assertReferenceDocumentIsCorrectlyWritten() {
        var adocRef = _Strings.readFromResource(this.getClass(), "/approved/" + adocSourceResourceLocation, StandardCharsets.UTF_8);
        var asciidoctor = Asciidoctor.Factory.create();
        var refDoc = asciidoctor.load(adocRef, Options.builder().build());

        if(debugEnabled) {
            System.out.println("==========================================");
            System.out.println("==  Adoc AST as read by Ref. Factory    ==");
            System.out.println("==========================================");
            _Debug.debug(refDoc);
            System.out.println("==========================================");
        }

        String actualAdoc = AsciiDocWriter.toString(refDoc);
        if(debugEnabled) {
            System.out.println("==========================================");
            System.out.println("== Generated Adoc Source (Ref. Factory) ==");
            System.out.println("==========================================");
            System.out.println(actualAdoc);
            System.out.println("==========================================");
        }

        _Text.assertTextEquals(adocRef, actualAdoc);
    }

    @Test
    void testReferenceDocumentIsCorrectlyWritten() throws IOException {
        if(!skipAsciidocjComplianceTest) {
            assertReferenceDocumentIsCorrectlyWritten();
        } else {
            System.err.printf("warning: skipping asciidocj compliance test for %s%n", this.getClass().getName());
        }
    }

}
