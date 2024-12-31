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
package org.apache.causeway.persistence.jdo.datanucleus.dialect;

import java.util.Optional;

import org.datanucleus.api.jdo.JDOAdapter;
import org.datanucleus.exceptions.NucleusException;

import org.springframework.dao.DataAccessException;

import org.apache.causeway.commons.functional.Try;
import org.apache.causeway.persistence.jdo.spring.integration.DefaultJdoDialect;
import org.apache.causeway.persistence.jdo.spring.integration.JdoDialect;

/**
 * Vendor (<i>Datanucleus</i>) specific implementation of <i>Spring's</i> {@link JdoDialect}
 * interface.
 *
 * @since 2.0 {@index}
 * @see JdoDialect
 */
public class DnJdoDialect extends DefaultJdoDialect {

    public DnJdoDialect(final Object connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public DataAccessException translateExceptionIfPossible(final RuntimeException cause) {

        var translatedException =

        Try.failure(cause)

        //XXX seems like a bug in DN, why do we need to unwrap this?
        .mapFailure(ex->ex instanceof IllegalArgumentException
                ? ((IllegalArgumentException)ex).getCause()
                : ex)

        // converts to JDOException
        .mapFailure(ex->ex instanceof NucleusException
                ? JDOAdapter
                        .getJDOExceptionForNucleusException(((NucleusException)ex))
                : ex)

        // converts to Spring's DataAccessException
        .mapFailure(ex->ex instanceof RuntimeException
                ? Optional.<Throwable>ofNullable(super.translateExceptionIfPossible((RuntimeException)ex))
                        .orElse(ex)
                : ex)

        .getFailure()
        .orElse(null);

        if(translatedException instanceof DataAccessException) {
            return (DataAccessException) translatedException;
        }

        // cannot translate
        return null;

    }

}
