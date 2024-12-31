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
package org.apache.causeway.valuetypes.markdown.applib.value;

import java.util.Arrays;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * @since 2.0 {@index}
 */
public final class Converter {

    /**
     * For syntax highlighting to work, the client/browser needs to load specific
     * Javascript and CSS.
     * The framework supports this out of the box with its various viewers,
     * using <i>Prism</i> web-jars.
     *
     * @param markdown - formated input to be converted to HTML
     *
     * @see <a href="https://prismjs.com/">prismjs.com</a>
     */
    public static String mdToHtml(final String markdown) {
        if(markdownSupport==null) {
            markdownSupport = new MarkdownSupport();
        }
        return markdownSupport.toHtml(markdown);
    }

    // -- HELPER

    private static MarkdownSupport markdownSupport;

    private static class MarkdownSupport {
        private Parser parser;
        private HtmlRenderer renderer;

        public MarkdownSupport() {
            MutableDataSet options = new MutableDataSet();

            // uncomment to set optional extensions
            options.set(Parser.EXTENSIONS, Arrays.asList(
                    TablesExtension.create(),
                    StrikethroughExtension.create()));

            // uncomment to convert soft-breaks to hard breaks
            //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

            parser = Parser.builder(options).build();
            renderer = HtmlRenderer.builder(options).build();
        }

        public String toHtml(final String markdown) {
            return renderer.render(parser.parse(markdown));
        }
    }

}
