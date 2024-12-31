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
package org.apache.causeway.security;

import org.apache.causeway.core.security.authentication.AuthenticationRequest;
import org.apache.causeway.core.security.authentication.standard.AuthenticatorAbstract;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticatorsForTesting {

    public static AuthenticatorAbstract authenticatorAllwaysValid() {
        return new AuthenticatorAbstract() {
            @Override
            public boolean canAuthenticate(final Class<? extends AuthenticationRequest> authenticationRequestClass) {
                return true;
            }

            @Override
            public boolean isValid(final AuthenticationRequest request) {
                return true;
            }
        };
    }

    public static AuthenticatorAbstract authenticatorNeverValid() {
        return new AuthenticatorAbstract() {
            @Override
            public boolean canAuthenticate(final Class<? extends AuthenticationRequest> authenticationRequestClass) {
                return true;
            }

            @Override
            public boolean isValid(final AuthenticationRequest request) {
                return false;
            }
        };
    }

    public static AuthenticatorAbstract authenticatorValidForFoo() {
        return new AuthenticatorAbstract() {
            @Override
            public boolean canAuthenticate(final Class<? extends AuthenticationRequest> authenticationRequestClass) {
                return true;
            }

            @Override
            public boolean isValid(final AuthenticationRequest request) {
                if(!"foo".equals(request.getName())) {
                    return false;
                }
                return true;
            }
        };
    }

}
