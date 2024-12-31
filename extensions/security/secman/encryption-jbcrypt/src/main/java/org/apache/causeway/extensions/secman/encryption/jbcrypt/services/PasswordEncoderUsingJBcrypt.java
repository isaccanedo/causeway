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
package org.apache.causeway.extensions.secman.encryption.jbcrypt.services;

import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.extensions.secman.applib.CausewayModuleExtSecmanApplib;

/**
 * @since 2.0 {@index}
 */
@Service
@Named(CausewayModuleExtSecmanApplib.NAMESPACE + ".PasswordEncoderUsingJBcrypt")
@javax.annotation.Priority(PriorityPrecedence.MIDPOINT)
@Qualifier("Secman")
public class PasswordEncoderUsingJBcrypt
implements PasswordEncoder {

    private String salt;

    private String getSalt() {
        if (salt == null) {
            salt = BCrypt.gensalt();
        }
        return salt;
    }

    @Override
    public String encode(final CharSequence rawPassword) {
        return rawPassword == null ? null : BCrypt.hashpw(rawPassword.toString(), getSalt());
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        if (rawPassword == null && encodedPassword == null) {
            return true;
        }
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
