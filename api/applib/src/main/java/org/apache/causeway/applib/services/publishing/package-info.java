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
 * The {@link org.apache.causeway.applib.services.publishing.spi.EntityPropertyChangeSubscriber} 
 * service provides a simple mechanism to capture changes to data. 
 * It is called for each property that has changed on any entity object 
 * (when enabled for publishing), as a set of pre- and post-values.
 * 
 * The {@link org.apache.causeway.applib.services.publishing.spi.ExecutionSubscriber} API is intended 
 * for coarse-grained publish/subscribe for system-to-system interactions, from Apache Causeway 
 * to some other system. Events that can be published are action invocations/property edits, 
 * and changed objects. A typical use case is to publish onto a pub/sub bus such as 
 * ActiveMQ with Camel to keep other systems up to date.
 * 
 * TODO v2 publishing/subscribing now has even more participants
 */
package org.apache.causeway.applib.services.publishing;
