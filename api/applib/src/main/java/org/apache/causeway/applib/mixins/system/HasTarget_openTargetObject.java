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
package org.apache.causeway.applib.mixins.system;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.applib.services.bookmark.HasTarget;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.metamodel.BeanSort;
import org.apache.causeway.applib.services.metamodel.MetaModelService;

import lombok.RequiredArgsConstructor;

/**
 * Provides the ability to navigate to a domain object from a
 * {@link DomainChangeRecord} which only holds the domain object by way of
 * a {@link DomainChangeRecord#getTarget() target}
 * {@link org.apache.causeway.applib.services.bookmark.Bookmark}.
 *
 * @since v2.0 {@index}
 */
@Action(
        commandPublishing = Publishing.DISABLED,
        executionPublishing = Publishing.DISABLED,
        semantics = SemanticsOf.SAFE
)
@ActionLayout(named = "Open", associateWith = "target", sequence = "1")
@RequiredArgsConstructor
public class HasTarget_openTargetObject {

    private final HasTarget hasTarget;

    @MemberSupport public Object act() {
        try {
            return bookmarkService != null
                    ? bookmarkService.lookup(hasTarget.getTarget()).orElse(null)
                    : null;
        } catch(RuntimeException ex) {
            if(ex.getClass().getName().contains("ObjectNotFoundException")) {
                messageService.warnUser("Object not found - has it since been deleted?");
                return null;
            }
            throw ex;
        }
    }

    @MemberSupport public boolean hideAct() {
        return hasTarget.getTarget() == null;
    }

    @MemberSupport public String disableAct() {
        final Object targetObject = hasTarget.getTarget();
        if (targetObject == null) {
            return null;
        }
        final BeanSort sortOfObject = metaModelService.sortOf(hasTarget.getTarget(), MetaModelService.Mode.RELAXED);
        return !(sortOfObject.isViewModel() || sortOfObject.isEntity())
                ? "Can only open view models or entities"
                : null;
    }

    @Inject BookmarkService bookmarkService;
    @Inject MessageService messageService;
    @Inject MetaModelService metaModelService;

}
