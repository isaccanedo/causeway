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
 * The {@link org.apache.causeway.applib.services.urlencoding.UrlEncodingService} defines a consistent way to convert strings to/from a form safe for use within a URL.
 * The service is used by the framework to map view model mementos (derived from the state of the view model itself)
 * into a form that can be used as a view model. When the framework needs to recreate the view model (for example to
 * invoke an action on it), this URL is converted back into a view model memento, from which the view model can then
 * be hydrated
 *
 *
 */
package org.apache.causeway.applib.services.urlencoding;