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
package org.apache.causeway.applib.graph.tree;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.causeway.commons.collections.Can;

class TreePathTest {

    @Test
    void rootConstructor() {
        final TreePath treePath = TreePath.root();
        assertThat(treePath.isRoot(), Matchers.is(true));
        assertThat(treePath.toString(), Matchers.is("/0"));
    }

    @Test
    void samePathsShouldBeEqual() {
        final TreePath treePath1 = TreePath.of(0, 1, 2, 3);
        final TreePath treePath2 = TreePath.of(0, 1, 2, 3);
        assertEquals(treePath1, treePath2);
        assertEquals(treePath1.hashCode(), treePath2.hashCode());
        assertEquals(treePath1.isRoot(), treePath2.isRoot());
        assertEquals(treePath1.toString(), treePath2.toString());

        assertThat(treePath1.toString(), Matchers.is("/0/1/2/3"));
    }

    @Test
    void hierarchyStreamingOfRoot() {
        assertEquals(
                Can.ofCollection(List.of("/0")),
                TreePath.root()
                    .streamUpTheHierarchyStartingAtSelf()
                    .map(TreePath::toString)
                    .collect(Can.toCan()));
    }

    @Test
    void hierarchyStreamingOfNonRoot() {
        assertEquals(
                Can.ofCollection(
                        List.of(
                                "/0/1/2/3",
                                "/0/1/2",
                                "/0/1",
                                "/0")),
                TreePath.of(0, 1, 2, 3)
                    .streamUpTheHierarchyStartingAtSelf()
                    .map(TreePath::toString)
                    .collect(Can.toCan()));
    }

}
