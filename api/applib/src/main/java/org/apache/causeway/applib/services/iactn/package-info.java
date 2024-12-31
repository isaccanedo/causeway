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

/**
 * The {@link org.apache.causeway.applib.services.iactn.InteractionProvider} is a request-scoped domain service that is used
 * to obtain the current {@link org.apache.causeway.applib.services.iactn.Interaction}.
 * An Interaction in turn generally consists of a single top-level Execution, either to invoke an action or to edit
 * a property. If that top-level action or property uses the {@link org.apache.causeway.applib.services.wrapper.WrapperFactory} to invoke child actions/properties, then
 * those sub-executions are captured as a call-graph. The Execution is thus a graph structure.
 *
 *
 */
package org.apache.causeway.applib.services.iactn;
