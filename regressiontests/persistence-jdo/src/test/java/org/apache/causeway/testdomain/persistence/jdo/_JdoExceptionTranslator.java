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
package org.apache.causeway.testdomain.persistence.jdo;

import java.util.NoSuchElementException;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOException;

import org.datanucleus.api.jdo.JDOAdapter;
import org.datanucleus.exceptions.NucleusException;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.commons.functional.Try;
import org.apache.causeway.persistence.jdo.spring.integration.JdoTransactionManager;

final class _JdoExceptionTranslator {

    // not used, but maybe keep for debugging purposes
    static DataAccessException translate(final Throwable failure, final JdoTransactionManager txManager) {

        return (DataAccessException) Try.failure(failure)

        //XXX seems like a bug in DN, why do we need to unwrap this?
        .mapFailure(ex->ex instanceof IllegalArgumentException
                ? ((IllegalArgumentException)ex).getCause()
                : ex)

        // asserts we have a NucleusException
        .ifFailure(ex->assertTrue(ex instanceof NucleusException))

        // converts to JDOException
        .mapFailure(ex->ex instanceof NucleusException
                ? JDOAdapter
                        .getJDOExceptionForNucleusException(((NucleusException)ex))
                : ex)

        // asserts translation to JDO standard
        .ifFailure(ex->assertTrue(ex instanceof JDODataStoreException))

        // converts to Spring DataAccessException
        .mapFailure(ex->ex instanceof JDOException
                ? txManager.getJdoDialect().translateException((JDOException)ex)
                : ex)

        .getFailure()
        .orElse(new NoSuchElementException("No value present"));

    }

}
