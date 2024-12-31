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
package org.apache.causeway.valuetypes.asciidoc.builder;

import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.ListItem;
import org.asciidoctor.ast.Section;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.ast.Table;

import org.apache.causeway.commons.internal.exceptions._Exceptions;

/**
 * Node visitor interface. Provide an implementing class to {@link StructuralNodeTraversor} to iterate through nodes.
 * <p>
 * This interface provides two methods, {@code head} and {@code tail}. The head method is called when the node is first
 * seen, and the tail method when all of the node's children have been visited. As an example, head can be used to
 * create a start tag for a node, and tail to create the end tag.
 * </p>
 */
interface StructuralNodeVisitor {

    // - HEAD

    /**
     * Callback for when a node is first visited.
     *
     * @param node the node being visited.
     * @param depth the depth of the node, relative to the root node. E.g., the root node has depth 0, and a child node
     * of that will have depth 1.
     */
    default boolean head(final StructuralNode node, final int depth) {
        if(node instanceof Document) {
            return documentHead((Document)node, depth);
        }
        if(node instanceof Table) {
            return tableHead((Table)node, depth);
        }
        if(node instanceof org.asciidoctor.ast.List) {
            return listHead((org.asciidoctor.ast.List)node, depth);
        }
        if(node instanceof ListItem) {
            return listItemHead((ListItem)node, depth);
        }
        if(node instanceof Block) {
            return blockHead((Block)node, depth);
        }
        if(node instanceof Section) {
            return sectionHead((Section)node, depth);
        }
        throw _Exceptions.unsupportedOperation("node type not supported %s", node.getClass());
    }

    // -- HEAD SPECIALISATIONS

    default boolean documentHead(final Document doc, final int depth) { return true; }

    default boolean blockHead(final Block block, final int depth) { return true; }

    default boolean sectionHead(final Section section, final int depth) { return true; }

    default boolean listHead(final org.asciidoctor.ast.List list, final int depth) { return true; }

    default boolean listItemHead(final ListItem listItem, final int depth) { return true; }

    default boolean tableHead(final Table table, final int depth) { return true; }

   // -- TAIL

    /**
     * Callback for when a node is last visited, after all of its descendants have been visited.
     *
     * @param node the node being visited.
     * @param depth the depth of the node, relative to the root node. E.g., the root node has depth 0, and a child node
     * of that will have depth 1.
     */
    default void tail(final StructuralNode node, final int depth) {
        if(node instanceof Document) {
            documentTail((Document)node, depth);
            return;
        }
        if(node instanceof Table) {
            tableTail((Table)node, depth);
            return;
        }
        if(node instanceof org.asciidoctor.ast.List) {
            listTail((org.asciidoctor.ast.List)node, depth);
            return;
        }
        if(node instanceof ListItem) {
            listItemTail((ListItem)node, depth);
            return;
        }
        if(node instanceof Block) {
            blockTail((Block)node, depth);
            return;
        }
        if(node instanceof Section) {
            sectionTail((Section)node, depth);
            return;
        }
        throw _Exceptions.unsupportedOperation("node type not supported %s", node.getClass());
    }

    // -- TAIL SPECIALISATIONS

    default void documentTail(final Document doc, final int depth) {}

    default void blockTail(final Block block, final int depth) {}

    default void sectionTail(final Section section, final int depth) {}

    default void listTail(final org.asciidoctor.ast.List list, final int depth) {}

    default void listItemTail(final ListItem listItem, final int depth) {}

    default void tableTail(final Table table, final int depth) {}

}
