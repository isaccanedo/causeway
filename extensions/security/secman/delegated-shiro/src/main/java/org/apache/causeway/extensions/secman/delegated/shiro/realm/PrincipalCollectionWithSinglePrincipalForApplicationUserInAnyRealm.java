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
 *
 */
package org.apache.causeway.extensions.secman.delegated.shiro.realm;

import java.util.Collection;

import org.apache.shiro.subject.SimplePrincipalCollection;

@SuppressWarnings("rawtypes")
public class PrincipalCollectionWithSinglePrincipalForApplicationUserInAnyRealm
extends SimplePrincipalCollection {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public void add(Object principal, String realmName) {
        if (realmName == null) {
            throw new IllegalArgumentException("realmName argument cannot be null.");
        }
        if (principal == null) {
            throw new IllegalArgumentException("principal argument cannot be null.");
        }
        final Collection principalsLazy = getPrincipalsLazy(realmName);
        if(principal instanceof PrincipalForApplicationUser) {
            principalsLazy.clear();
        }
        principalsLazy.add(principal);
    }

    @Override
    public void addAll(Collection principals, String realmName) {
        for (Object principal : principals) {
            add(principal, realmName);
        }
    }

}
