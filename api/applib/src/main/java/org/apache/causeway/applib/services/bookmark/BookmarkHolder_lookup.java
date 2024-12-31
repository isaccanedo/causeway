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
package org.apache.causeway.applib.services.bookmark;

import javax.inject.Inject;

import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;

import lombok.RequiredArgsConstructor;

/**
 * @since 1.x {@index}
 */
@Action(
        commandPublishing = Publishing.DISABLED,
        domainEvent = BookmarkHolder_lookup.ActionDomainEvent.class,
        executionPublishing = Publishing.DISABLED,
        semantics = SemanticsOf.SAFE
)
@ActionLayout(
        cssClassFa = "fa-bookmark"
)
@RequiredArgsConstructor
public class BookmarkHolder_lookup {

    private final BookmarkHolder bookmarkHolder;

    public static class ActionDomainEvent extends CausewayModuleApplib.ActionDomainEvent<BookmarkHolder_lookup> {}

    @MemberSupport public Object act() {
        return bookmarkService.lookup(bookmarkHolder).orElse(null);
    }

    // -- DEPENDENCIES

    @Inject private BookmarkService bookmarkService;

}
